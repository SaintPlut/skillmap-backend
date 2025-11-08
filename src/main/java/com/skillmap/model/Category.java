package com.skillmap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    private String color;

    @ManyToMany(mappedBy = "categories")
    private List<SkillMap> skillMaps = new ArrayList<>();

    // Constructors
    public Category() {}

    public Category(String name, String color) {
        this.name = name;
        this.color = color;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public List<SkillMap> getSkillMaps() { return skillMaps; }
    public void setSkillMaps(List<SkillMap> skillMaps) { this.skillMaps = skillMaps; }
}