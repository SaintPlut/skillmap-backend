package com.skillmap.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SkillMapService {

    @Autowired
    private JsonDataService jsonDataService;

    public List<Map<String, Object>> getUserSkillMaps(Long userId) {
        return jsonDataService.getSkillMapsByUser(userId);
    }

    public Map<String, Object> createSkillMap(String title, String description, Long ownerId) {
        Map<String, Object> skillMap = new HashMap<>();
        skillMap.put("id", System.currentTimeMillis());
        skillMap.put("title", title);
        skillMap.put("description", description);
        skillMap.put("ownerId", ownerId);
        skillMap.put("createdAt", new Date().toString());
        skillMap.put("nodes", new ArrayList<>());

        jsonDataService.saveSkillMap(skillMap);
        return skillMap;
    }

    public Map<String, Object> getSkillMap(Long mapId) {
        return jsonDataService.getSkillMapsByUser(null).stream()
                .filter(map -> mapId.equals(map.get("id")))
                .findFirst()
                .orElse(null);
    }
}