package com.skillmap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_progress", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "node_id"})
})
public class UserProgress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "node_id", nullable = false)
    private SkillNode skillNode;

    @Min(0)
    @Max(100)
    private Integer progress = 0;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Constructors
    public UserProgress() {}

    public UserProgress(User user, SkillNode skillNode, Integer progress) {
        this.user = user;
        this.skillNode = skillNode;
        this.progress = progress;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public SkillNode getSkillNode() { return skillNode; }
    public void setSkillNode(SkillNode skillNode) { this.skillNode = skillNode; }

    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}