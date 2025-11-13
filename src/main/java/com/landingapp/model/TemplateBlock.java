package com.landingapp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import javax.persistence.*;

@Entity
@Table(name = "template_blocks")
@Data
public class TemplateBlock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "block_id", nullable = false)
    private String blockId;

    @Column(nullable = false)
    private String type;

    private String label;

    @Column(columnDefinition = "TEXT")
    private String defaultValue;

    private Boolean required = false;

    @Column(name = "\"order\"")
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id")
    @JsonIgnoreProperties("editableBlocks")
    private Template template;
}