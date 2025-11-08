package com.skillmap.controller;

import com.skillmap.service.SkillMapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/maps")
@CrossOrigin(origins = "http://localhost:3000")
public class SkillMapController {

    @Autowired
    private SkillMapService skillMapService;

    @GetMapping
    public List<Map<String, Object>> getUserMaps(@RequestHeader("User-ID") Long userId) {
        System.out.println("🗺 Get maps for user: " + userId);
        return skillMapService.getUserSkillMaps(userId);
    }

    @PostMapping
    public ResponseEntity<?> createSkillMap(
            @RequestBody Map<String, String> request,
            @RequestHeader("User-ID") Long userId) {
        try {
            System.out.println("🗺 Create map for user: " + userId);

            Map<String, Object> skillMap = skillMapService.createSkillMap(
                    request.get("title"),
                    request.get("description"),
                    userId
            );

            return ResponseEntity.ok(skillMap);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getSkillMap(@PathVariable Long id) {
        try {
            Map<String, Object> skillMap = skillMapService.getSkillMap(id);
            if (skillMap != null) {
                return ResponseEntity.ok(skillMap);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}