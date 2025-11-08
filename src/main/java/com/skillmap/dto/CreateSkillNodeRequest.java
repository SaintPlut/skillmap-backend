package com.skillmap.dto;

public class CreateSkillNodeRequest {
    private String title;
    private String description;
    private Integer progress;
    private Long parentId;

    // Constructors
    public CreateSkillNodeRequest() {}

    public CreateSkillNodeRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }

    public Long getParentId() { return parentId; }
    public void setParentId(Long parentId) { this.parentId = parentId; }
}