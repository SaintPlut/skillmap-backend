package com.skillmap.dto;

import java.util.ArrayList;
import java.util.List;

public class CreateSkillMapRequest {
    private String title;
    private String description;
    private List<CreateSkillNodeRequest> nodes = new ArrayList<>();

    // Constructors
    public CreateSkillMapRequest() {}

    public CreateSkillMapRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<CreateSkillNodeRequest> getNodes() { return nodes; }
    public void setNodes(List<CreateSkillNodeRequest> nodes) { this.nodes = nodes; }
}