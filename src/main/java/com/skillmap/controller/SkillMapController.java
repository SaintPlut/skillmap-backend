package com.skillmap.controller;

import com.skillmap.dto.CreateSkillMapRequest;
import com.skillmap.dto.SkillMapDTO;
import com.skillmap.model.SkillMap;
import com.skillmap.model.SkillNode;
import com.skillmap.service.SkillMapService;
import com.skillmap.service.SkillNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/maps")
@CrossOrigin(origins = "http://localhost:3000")
public class SkillMapController {

    @Autowired
    private SkillMapService skillMapService;

    @Autowired
    private SkillNodeService skillNodeService;

    // API 3: Получить все карты пользователя
    @GetMapping
    public ResponseEntity<List<SkillMapDTO>> getUserMaps(@RequestHeader("User-ID") Long userId) {
        List<SkillMap> maps = skillMapService.getUserSkillMaps(userId);
        List<SkillMapDTO> dtos = maps.stream()
                .map(skillMapService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // API 4: Получить конкретную карту
    @GetMapping("/{id}")
    public ResponseEntity<?> getSkillMap(@PathVariable Long id) {
        return skillMapService.getSkillMapById(id)
                .map(skillMap -> {
                    SkillMapDTO dto = skillMapService.convertToDTO(skillMap);
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // API 5: Создать новую карту
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createSkillMap(
            @RequestBody CreateSkillMapRequest request,
            @RequestHeader("User-ID") Long userId) {
        try {
            System.out.println("Получен запрос на создание карты: " + request.getTitle());
            System.out.println("User-ID: " + userId);

            // Валидация
            if (request.getTitle() == null || request.getTitle().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Название карты обязательно"));
            }

            // Создаем базовую карту
            SkillMap skillMap = new SkillMap();
            skillMap.setTitle(request.getTitle().trim());
            skillMap.setDescription(request.getDescription() != null ? request.getDescription().trim() : "");

            SkillMap createdMap = skillMapService.createSkillMap(skillMap, userId);
            return ResponseEntity.ok(createdMap);
        } catch (Exception e) {
            System.err.println("Ошибка при создании карты: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", "Ошибка при создании карты: " + e.getMessage()));
        }
    }

    // API 6: Обновить карту
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateSkillMap(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        try {
            String title = request.get("title");
            String description = request.get("description");

            if (title == null || title.trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Название карты обязательно"));
            }

            SkillMap skillMap = new SkillMap();
            skillMap.setTitle(title.trim());
            skillMap.setDescription(description != null ? description.trim() : "");

            SkillMap updatedMap = skillMapService.updateSkillMap(id, skillMap);
            return ResponseEntity.ok(updatedMap);
        } catch (Exception e) {
            System.err.println("Ошибка при обновлении карты: " + e.getMessage());
            return ResponseEntity.badRequest().body(Map.of("error", "Ошибка при обновлении карты: " + e.getMessage()));
        }
    }

    // API 7: Удалить карту
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteSkillMap(@PathVariable Long id) {
        try {
            skillMapService.deleteSkillMap(id);
            return ResponseEntity.ok().body(Map.of("message", "SkillMap deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // API 8: Поиск карт
    @GetMapping("/search")
    public ResponseEntity<List<SkillMap>> searchSkillMaps(@RequestParam String q) {
        List<SkillMap> maps = skillMapService.searchSkillMaps(q);
        return ResponseEntity.ok(maps);
    }

    // API 9: Добавить узел к карте
    @PostMapping(value = "/{mapId}/nodes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addSkillNode(
            @PathVariable Long mapId,
            @RequestBody SkillNode skillNode) {
        try {
            SkillNode createdNode = skillMapService.addSkillNodeToMap(mapId, skillNode);
            return ResponseEntity.ok(createdNode);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // API 10: Получить узлы карты
    @GetMapping("/{mapId}/nodes")
    public ResponseEntity<List<SkillNode>> getMapNodes(@PathVariable Long mapId) {
        List<SkillNode> nodes = skillNodeService.getNodesByMapId(mapId);
        return ResponseEntity.ok(nodes);
    }
}