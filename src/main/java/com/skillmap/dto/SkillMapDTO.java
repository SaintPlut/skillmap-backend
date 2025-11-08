package com.skillmap.dto;

import java.time.LocalDateTime;
import java.util.List;

public class SkillMapDTO {
    private Long id;
    private String title;
    private String description;
    private Long ownerId;
    private String ownerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SkillNodeDTO> nodes;
    private List<CategoryDTO> categories;
    private Integer nodeCount;
    private Integer totalProgress;

    // Constructors, Getters and Setters
    public SkillMapDTO() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getOwnerId() { return ownerId; }
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }

    public String getOwnerName() { return ownerName; }
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<SkillNodeDTO> getNodes() { return nodes; }
    public void setNodes(List<SkillNodeDTO> nodes) { this.nodes = nodes; }

    public List<CategoryDTO> getCategories() { return categories; }
    public void setCategories(List<CategoryDTO> categories) { this.categories = categories; }

    public Integer getNodeCount() { return nodeCount; }
    public void setNodeCount(Integer nodeCount) { this.nodeCount = nodeCount; }

    public Integer getTotalProgress() { return totalProgress; }
    public void setTotalProgress(Integer totalProgress) { this.totalProgress = totalProgress; }
}