package com.skillmap.service;

import com.skillmap.dto.SkillMapDTO;
import com.skillmap.model.SkillMap;
import com.skillmap.model.SkillNode;
import com.skillmap.model.User;
import com.skillmap.repository.SkillMapRepository;
import com.skillmap.repository.SkillNodeRepository;
import com.skillmap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SkillMapService {

    @Autowired
    private SkillMapRepository skillMapRepository;

    @Autowired
    private SkillNodeRepository skillNodeRepository;

    @Autowired
    private UserRepository userRepository;

    public List<SkillMap> getUserSkillMaps(Long userId) {
        return skillMapRepository.findByOwnerId(userId);
    }

    public Optional<SkillMap> getSkillMapById(Long id) {
        return skillMapRepository.findById(id);
    }

    public SkillMap createSkillMap(SkillMap skillMap, Long ownerId) {
        User owner = userRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Создаем новую карту без узлов сначала
        SkillMap newMap = new SkillMap();
        newMap.setTitle(skillMap.getTitle());
        newMap.setDescription(skillMap.getDescription());
        newMap.setOwner(owner);

        // Сохраняем карту без узлов
        SkillMap savedMap = skillMapRepository.save(newMap);

        // Теперь добавляем узлы, если они есть
        if (skillMap.getNodes() != null && !skillMap.getNodes().isEmpty()) {
            for (SkillNode node : skillMap.getNodes()) {
                SkillNode newNode = new SkillNode();
                newNode.setTitle(node.getTitle());
                newNode.setDescription(node.getDescription());
                newNode.setProgress(node.getProgress() != null ? node.getProgress() : 0);
                newNode.setSkillMap(savedMap);

                // Сохраняем узел
                skillNodeRepository.save(newNode);
            }
        }

        return savedMap;
    }

    public SkillMap updateSkillMap(Long id, SkillMap updatedMap) {
        return skillMapRepository.findById(id)
                .map(map -> {
                    map.setTitle(updatedMap.getTitle());
                    map.setDescription(updatedMap.getDescription());
                    return skillMapRepository.save(map);
                })
                .orElseThrow(() -> new RuntimeException("SkillMap not found"));
    }

    public void deleteSkillMap(Long id) {
        skillMapRepository.deleteById(id);
    }

    public List<SkillMap> searchSkillMaps(String query) {
        return skillMapRepository.findByTitleContainingIgnoreCase(query);
    }

    public SkillNode addSkillNodeToMap(Long mapId, SkillNode skillNode) {
        SkillMap skillMap = skillMapRepository.findById(mapId)
                .orElseThrow(() -> new RuntimeException("SkillMap not found"));

        // Создаем новый узел
        SkillNode newNode = new SkillNode();
        newNode.setTitle(skillNode.getTitle());
        newNode.setDescription(skillNode.getDescription());
        newNode.setProgress(skillNode.getProgress() != null ? skillNode.getProgress() : 0);
        newNode.setSkillMap(skillMap);

        // Обрабатываем родительский узел, если указан
        if (skillNode.getParent() != null && skillNode.getParent().getId() != null) {
            SkillNode parent = skillNodeRepository.findById(skillNode.getParent().getId())
                    .orElseThrow(() -> new RuntimeException("Parent node not found"));
            newNode.setParent(parent);
        }

        return skillNodeRepository.save(newNode);
    }

    // Преобразование Entity в DTO
    public SkillMapDTO convertToDTO(SkillMap skillMap) {
        SkillMapDTO dto = new SkillMapDTO();
        dto.setId(skillMap.getId());
        dto.setTitle(skillMap.getTitle());
        dto.setDescription(skillMap.getDescription());
        dto.setOwnerId(skillMap.getOwner().getId());
        dto.setOwnerName(skillMap.getOwner().getName());
        dto.setCreatedAt(skillMap.getCreatedAt());
        dto.setUpdatedAt(skillMap.getUpdatedAt());
        dto.setNodeCount(skillMap.getNodes().size());

        // Расчет общего прогресса
        if (!skillMap.getNodes().isEmpty()) {
            double avgProgress = skillMap.getNodes().stream()
                    .mapToInt(SkillNode::getProgress)
                    .average()
                    .orElse(0.0);
            dto.setTotalProgress((int) avgProgress);
        }

        return dto;
    }
}