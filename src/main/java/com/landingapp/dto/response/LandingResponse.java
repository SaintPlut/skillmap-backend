package com.landingapp.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class LandingResponse {
    private Long id;
    private String name;
    private String templateId;
    private List<BlockResponse> blocks;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
