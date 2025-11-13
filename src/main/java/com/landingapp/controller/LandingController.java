package com.landingapp.controller;

import com.landingapp.dto.request.CreateLandingRequest;
import com.landingapp.dto.response.LandingResponse;
import com.landingapp.service.LandingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/landings")
@Tag(name = "Landings")
public class LandingController {

    @Autowired
    private LandingService landingService;

    @GetMapping
    @Operation(summary = "Получить все лендинги с фильтрацией")
    public ResponseEntity<List<LandingResponse>> getLandings(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String templateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<LandingResponse> landings = landingService.getLandings(search, templateId, page, size);
        return ResponseEntity.ok(landings);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить лендинг по ID")
    public ResponseEntity<LandingResponse> getLanding(@PathVariable Long id) {
        LandingResponse landing = landingService.getLandingById(id);
        return ResponseEntity.ok(landing);
    }

    @PostMapping
    @Operation(summary = "Создать новый лендинг")
    public ResponseEntity<LandingResponse> createLanding(
            @Valid @RequestBody CreateLandingRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        LandingResponse landing = landingService.createLanding(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(landing);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить лендинг")
    public ResponseEntity<LandingResponse> updateLanding(
            @PathVariable Long id,
            @Valid @RequestBody CreateLandingRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        LandingResponse landing = landingService.updateLanding(id, request, userDetails.getUsername());
        return ResponseEntity.ok(landing);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить лендинг")
    public ResponseEntity<Void> deleteLanding(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        landingService.deleteLanding(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/my")
    @Operation(summary = "Получить мои лендинги")
    public ResponseEntity<List<LandingResponse>> getMyLandings(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<LandingResponse> landings = landingService.getUserLandings(userDetails.getUsername(), page, size);
        return ResponseEntity.ok(landings);
    }
}
