// controller/AdminController.java
package com.landingapp.controller;

import com.landingapp.dto.response.*;
import com.landingapp.model.Landing;
import com.landingapp.model.User;
import com.landingapp.service.LandingService;
import com.landingapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "API для административных операций")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    private final UserService userService;
    private final LandingService landingService;

    public AdminController(UserService userService, LandingService landingService) {
        this.userService = userService;
        this.landingService = landingService;
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsResponse> getAdminStats() {
        long totalUsers = userService.getTotalUsersCount();
        long totalLandings = landingService.getTotalLandingsCount();
        long publishedLandings = landingService.getPublishedLandingsCount();

        // Используем DTO для recentLandings
        List<AdminLandingResponse> recentLandings = landingService.getRecentLandings(5)
                .stream()
                .map(AdminLandingResponse::new)
                .collect(Collectors.toList());

        AdminStatsResponse stats = AdminStatsResponse.builder()
                .totalUsers(totalUsers)
                .totalLandings(totalLandings)
                .publishedLandings(publishedLandings)
                .recentLandings(recentLandings) // теперь это DTO
                .build();

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/users")
    public ResponseEntity<List<AdminUserResponse>> getUsers() {
        List<User> users = userService.getAllUsers();

        List<AdminUserResponse> adminUsers = users.stream()
                .map(user -> {
                    long landingsCount = landingService.getUserLandingsCount(user.getId());
                    return AdminUserResponse.builder()
                            .id(user.getId().toString())
                            .username(user.getUsername())
                            .email(user.getEmail())
                            .landingsCount(landingsCount)
                            .createdAt(user.getCreatedAt())
                            .lastActivity(user.getUpdatedAt())
                            .build();
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(adminUsers);
    }

    // FIXED: Используем DTO вместо Entity
    @GetMapping("/landings")
    public ResponseEntity<List<AdminLandingResponse>> getAllLandings() {
        List<Landing> landings = landingService.getAllLandings();

        List<AdminLandingResponse> landingResponses = landings.stream()
                .map(AdminLandingResponse::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(landingResponses);
    }
}