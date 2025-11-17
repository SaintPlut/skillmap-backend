package com.landingapp.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AdminUserResponse {
    private String id;
    private String username;
    private String email;
    private Long landingsCount;
    private LocalDateTime createdAt;
    private LocalDateTime lastActivity;
}
