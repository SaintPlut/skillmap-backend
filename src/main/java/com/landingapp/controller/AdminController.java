// controller/AdminController.java
package com.landingapp.controller;

import com.landingapp.dto.response.ApiResponse;
import com.landingapp.dto.response.LandingResponse;
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

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin", description = "API для административных операций")
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
public class AdminController {

    @Autowired
    private LandingService landingService;

    @Autowired
    private UserService userService;

    @GetMapping("/landings")
    @Operation(summary = "Получить все лендинги пользователей")
    public ResponseEntity<List<LandingResponse>> getAllUserLandings(
            @Parameter(description = "Номер страницы (начинается с 0)", example = "0")
            @RequestParam(defaultValue = "0") @Min(0) int page,

            @Parameter(description = "Размер страницы (1-100)", example = "20")
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size,

            @Parameter(description = "Поиск по названию лендинга или автору", example = "бизнес")
            @RequestParam(required = false) String search,

            @Parameter(description = "Фильтр по ID шаблона", example = "1")
            @RequestParam(required = false) String templateId) {

        List<LandingResponse> landings = landingService.getAllLandings(search, templateId, page, size);
        return ResponseEntity.ok(landings);
    }

    @GetMapping("/statistics")
    @Operation(summary = "Получить статистику системы")
    public ResponseEntity<AdminStatisticsResponse> getStatistics() {
        // Используем существующие методы репозиториев через сервисы
        long totalLandings = landingService.getTotalLandingsCount();
        long totalUsers = userService.getTotalUsersCount();

        AdminStatisticsResponse statistics = new AdminStatisticsResponse(
                totalLandings,
                totalUsers,
                totalLandings, // активные лендинги = всего лендингов
                "1.0.0"
        );
        return ResponseEntity.ok(statistics);
    }

    @DeleteMapping("/landings/{id}")
    @Operation(summary = "Удалить лендинг")
    public ResponseEntity<ApiResponse> deleteLanding(
            @Parameter(description = "ID лендинга для удаления", example = "1")
            @PathVariable Long id) {

        landingService.deleteLanding(id);
        return ResponseEntity.ok(new ApiResponse("Лендинг успешно удален"));
    }

    @GetMapping("/landings/{id}")
    @Operation(summary = "Получить лендинг по ID")
    public ResponseEntity<LandingResponse> getLandingById(
            @Parameter(description = "ID лендинга", example = "1")
            @PathVariable Long id) {

        LandingResponse landing = landingService.getLandingById(id);
        return ResponseEntity.ok(landing);
    }

    @GetMapping("/users/{userId}/landings")
    @Operation(summary = "Получить лендинги пользователя")
    public ResponseEntity<List<LandingResponse>> getUserLandings(
            @Parameter(description = "ID пользователя", example = "1")
            @PathVariable Long userId,

            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {

        List<LandingResponse> landings = landingService.getLandingsByUserId(userId, page, size);
        return ResponseEntity.ok(landings);
    }

    public static class AdminStatisticsResponse {
        private long totalLandings;
        private long totalUsers;
        private long activeLandings;
        private String systemVersion;

        public AdminStatisticsResponse() {}

        public AdminStatisticsResponse(long totalLandings, long totalUsers, long activeLandings, String systemVersion) {
            this.totalLandings = totalLandings;
            this.totalUsers = totalUsers;
            this.activeLandings = activeLandings;
            this.systemVersion = systemVersion;
        }

        public long getTotalLandings() { return totalLandings; }
        public void setTotalLandings(long totalLandings) { this.totalLandings = totalLandings; }

        public long getTotalUsers() { return totalUsers; }
        public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

        public long getActiveLandings() { return activeLandings; }
        public void setActiveLandings(long activeLandings) { this.activeLandings = activeLandings; }

        public String getSystemVersion() { return systemVersion; }
        public void setSystemVersion(String systemVersion) { this.systemVersion = systemVersion; }
    }
}