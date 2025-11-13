package com.landingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "templates")
@Data
public class Template {
    @Id
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String author;
    private String license;
    private Double price;

    @Column(columnDefinition = "TEXT")
    private String previewImage;

    @ElementCollection
    @CollectionTable(name = "template_keywords", joinColumns = @JoinColumn(name = "template_id"))
    @Column(name = "keyword")
    private List<String> keywords = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "template", fetch = FetchType.LAZY)
    @OrderBy("order ASC")
    @JsonIgnoreProperties("template")
    private List<TemplateBlock> editableBlocks = new ArrayList<>();
}
