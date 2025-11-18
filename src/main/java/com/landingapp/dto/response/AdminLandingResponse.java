package com.landingapp.dto.response;

import com.landingapp.model.Landing;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AdminLandingResponse {
    private Long id;
    private String name;
    private String templateId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean published;
    private String authorUsername;
    private String authorEmail;

    public AdminLandingResponse(Landing landing) {
        this.id = landing.getId();
        this.name = landing.getName();
        this.templateId = landing.getTemplateId();
        this.createdAt = landing.getCreatedAt();
        this.updatedAt = landing.getUpdatedAt();
        this.published = landing.getPublished();
        this.authorUsername = landing.getUser().getUsername();
        this.authorEmail = landing.getUser().getEmail();
    }
}
