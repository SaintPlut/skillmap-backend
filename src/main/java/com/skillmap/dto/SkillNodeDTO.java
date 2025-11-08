package com.skillmap.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SkillNodeDTO {
    private Long id;
    private String title;
    private String description;
    private Integer progress;
    private Long parentId;
    private Long mapId;
    private LocalDateTime createdAt;
    private List<SkillNodeDTO> children;
    private Integer userProgress;

    // Constructors, Getters and Setters
    public SkillNodeDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }

    public Long getMapId() { return mapId; }
    public void setMapId(Long mapId) { this.mapId = mapId; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public List<SkillNodeDTO> getChildren() { return children; }
    public void setChildren(List<SkillNodeDTO> children) { this.children = children; }

    public Integer getUserProgress() { return userProgress; }
    public void setUserProgress(Integer userProgress) { this.userProgress = userProgress; }
}