package com.skillmap.model;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_starred_maps", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "map_id"})
})
public class UserStarredMap {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "map_id", nullable = false)
    private SkillMap skillMap;

    @CreationTimestamp
    private LocalDateTime starredAt;

    // Constructors
    public UserStarredMap() {}

    public UserStarredMap(User user, SkillMap skillMap) {
        this.user = user;
        this.skillMap = skillMap;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public SkillMap getSkillMap() { return skillMap; }
    public void setSkillMap(SkillMap skillMap) { this.skillMap = skillMap; }

    public LocalDateTime getStarredAt() { return starredAt; }
    public void setStarredAt(LocalDateTime starredAt) { this.starredAt = starredAt; }
}