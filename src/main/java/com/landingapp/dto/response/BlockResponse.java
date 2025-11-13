package com.landingapp.dto.response;

import lombok.Data;

@Data
public class BlockResponse {
    private String id;
    private String type;
    private String content;
    private Integer order;
}
