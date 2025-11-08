package com.skillmap.controller;

import com.skillmap.model.SkillNode;
import com.skillmap.service.SkillNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/nodes")
@CrossOrigin(origins = "http://localhost:3000")
public class SkillNodeController {

    @Autowired
    private SkillNodeService skillNodeService;

    // API 11: Обновить прогресс узла
    @PutMapping(value = "/{id}/progress", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<SkillNode> updateNodeProgress(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> request) {
        Integer progress = request.get("progress");
        SkillNode updatedNode = skillNodeService.updateNodeProgress(id, progress);
        return ResponseEntity.ok(updatedNode);
    }

    // API 12: Удалить узел
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNode(@PathVariable Long id) {
        skillNodeService.deleteNode(id);
        return ResponseEntity.ok().body(Map.of("message", "Node deleted successfully"));
    }
}