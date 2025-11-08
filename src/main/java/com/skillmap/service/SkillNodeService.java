package com.skillmap.service;

import com.skillmap.model.SkillNode;
import com.skillmap.repository.SkillNodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SkillNodeService {

    @Autowired
    private SkillNodeRepository skillNodeRepository;

    public List<SkillNode> getNodesByMapId(Long mapId) {
        return skillNodeRepository.findBySkillMapId(mapId);
    }

    public List<SkillNode> getRootNodesByMapId(Long mapId) {
        return skillNodeRepository.findRootNodesByMapId(mapId);
    }

    public Optional<SkillNode> getNodeById(Long id) {
        return skillNodeRepository.findById(id);
    }

    public SkillNode updateNodeProgress(Long nodeId, Integer progress) {
        return skillNodeRepository.findById(nodeId)
                .map(node -> {
                    node.setProgress(progress);
                    return skillNodeRepository.save(node);
                })
                .orElseThrow(() -> new RuntimeException("SkillNode not found"));
    }

    public void deleteNode(Long id) {
        skillNodeRepository.deleteById(id);
    }
}