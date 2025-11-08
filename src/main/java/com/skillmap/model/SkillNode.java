package com.skillmap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "skill_nodes")
public class SkillNode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    private String description;

    @Min(0)
    @Max(100)
    private Integer progress = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private SkillNode parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<SkillNode> children = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id", nullable = false)
    private SkillMap skillMap;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Constructors
    public SkillNode() {}

    public SkillNode(String title, String description, SkillMap skillMap) {
        this.title = title;
        this.description = description;
        this.skillMap = skillMap;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }

    public SkillNode getParent() { return parent; }
    public void setParent(SkillNode parent) { this.parent = parent; }

    public List<SkillNode> getChildren() { return children; }
    public void setChildren(List<SkillNode> children) { this.children = children; }

    public SkillMap getSkillMap() { return skillMap; }
    public void setSkillMap(SkillMap skillMap) { this.skillMap = skillMap; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}