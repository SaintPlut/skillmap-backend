package com.landingapp.controller;

import com.landingapp.model.Template;
import com.landingapp.service.TemplateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/templates")
@Tag(name = "Templates")
public class TemplateController {

    @Autowired
    private TemplateService templateService;

    @GetMapping
    @Operation(summary = "Получить все шаблоны")
    public ResponseEntity<List<Template>> getTemplates() {
        List<Template> templates = templateService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить шаблон по ID")
    public ResponseEntity<Template> getTemplate(@PathVariable String id) {
        Optional<Template> template = templateService.getTemplateById(id);
        return template.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать новый шаблон")
    public ResponseEntity<Template> createTemplate(@RequestBody Template template) {
        Template savedTemplate = templateService.createTemplate(template);
        return ResponseEntity.status(201).body(savedTemplate);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить шаблон")
    public ResponseEntity<Template> updateTemplate(
            @PathVariable String id,
            @RequestBody Template template) {

        Template updatedTemplate = templateService.updateTemplate(id, template);
        return ResponseEntity.ok(updatedTemplate);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить шаблон")
    public ResponseEntity<Void> deleteTemplate(@PathVariable String id) {
        templateService.deleteTemplate(id);
        return ResponseEntity.noContent().build();
    }
}
