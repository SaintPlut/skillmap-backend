package com.landingapp.service;

import com.landingapp.dto.request.CreateLandingRequest;
import com.landingapp.dto.response.BlockResponse;
import com.landingapp.dto.response.LandingResponse;
import com.landingapp.model.Block;
import com.landingapp.model.Landing;
import com.landingapp.model.User;
import com.landingapp.exception.ResourceNotFoundException;
import com.landingapp.repository.LandingRepository;
import com.landingapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.persistence.criteria.Predicate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LandingService {

    @Autowired
    private LandingRepository landingRepository;

    @Autowired
    private UserRepository userRepository;

    public List<LandingResponse> getLandings(String search, String templateId, int page, int size) {
        Specification<Landing> spec = (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            if (StringUtils.hasText(search)) {
                predicate = cb.and(predicate, cb.or(
                        cb.like(cb.lower(root.get("name")), "%" + search.toLowerCase() + "%"),
                        cb.like(cb.lower(root.get("templateId")), "%" + search.toLowerCase() + "%")
                ));
            }

            if (StringUtils.hasText(templateId)) {
                predicate = cb.and(predicate, cb.equal(root.get("templateId"), templateId));
            }

            return predicate;
        };

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Landing> landings = landingRepository.findAll(spec, pageable);

        return landings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<LandingResponse> getAllLandings(String search, String templateId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Landing> landings = landingRepository.findAll(pageable);

        return landings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public LandingResponse createLanding(CreateLandingRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Landing landing = new Landing();
        landing.setName(request.getName());
        landing.setTemplateId(request.getTemplateId());
        landing.setUser(user);

        List<Block> blocks = request.getBlocks().stream()
                .map(blockRequest -> {
                    Block block = new Block();
                    block.setBlockId(blockRequest.getBlockId());
                    block.setType(blockRequest.getType());
                    block.setContent(blockRequest.getContent());
                    block.setOrder(blockRequest.getOrder());
                    block.setLanding(landing);
                    return block;
                })
                .collect(Collectors.toList());

        landing.setBlocks(blocks);
        Landing saved = landingRepository.save(landing);

        return convertToResponse(saved);
    }

    public LandingResponse updateLanding(Long id, CreateLandingRequest request, String username) {
        Landing landing = landingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Лендинг не найден"));

        if (!landing.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Нет прав для редактирования этого лендинга");
        }

        landing.setName(request.getName());
        landing.setTemplateId(request.getTemplateId());

        landing.getBlocks().clear();
        List<Block> blocks = request.getBlocks().stream()
                .map(blockRequest -> {
                    Block block = new Block();
                    block.setBlockId(blockRequest.getBlockId());
                    block.setType(blockRequest.getType());
                    block.setContent(blockRequest.getContent());
                    block.setOrder(blockRequest.getOrder());
                    block.setLanding(landing);
                    return block;
                })
                .collect(Collectors.toList());

        landing.getBlocks().addAll(blocks);
        Landing updated = landingRepository.save(landing);

        return convertToResponse(updated);
    }

    public void deleteLanding(Long id, String username) {
        Landing landing = landingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Лендинг не найден"));

        if (!landing.getUser().getUsername().equals(username)) {
            throw new RuntimeException("Нет прав для удаления этого лендинга");
        }

        landingRepository.delete(landing);
    }

    public void deleteLanding(Long id) {
        if (!landingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Лендинг не найден");
        }
        landingRepository.deleteById(id);
    }

    public LandingResponse getLandingById(Long id) {
        Landing landing = landingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Лендинг не найден"));
        return convertToResponse(landing);
    }

    public List<LandingResponse> getUserLandings(String username, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<Landing> landings = landingRepository.findByUsername(username);

        return landings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<LandingResponse> getLandingsByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<Landing> landings = landingRepository.findByUserId(userId);

        return landings.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public long getTotalLandingsCount() {
        return landingRepository.count();
    }

    private LandingResponse convertToResponse(Landing landing) {
        LandingResponse response = new LandingResponse();
        response.setId(landing.getId());
        response.setName(landing.getName());
        response.setTemplateId(landing.getTemplateId());
        response.setAuthor(landing.getUser().getUsername());
        response.setCreatedAt(landing.getCreatedAt());
        response.setUpdatedAt(landing.getUpdatedAt());

        List<BlockResponse> blockResponses = landing.getBlocks().stream()
                .map(block -> {
                    BlockResponse blockResponse = new BlockResponse();
                    blockResponse.setId(block.getBlockId());
                    blockResponse.setType(block.getType());
                    blockResponse.setContent(block.getContent());
                    blockResponse.setOrder(block.getOrder());
                    return blockResponse;
                })
                .sorted(Comparator.comparing(BlockResponse::getOrder))
                .collect(Collectors.toList());

        response.setBlocks(blockResponses);
        return response;
    }
}