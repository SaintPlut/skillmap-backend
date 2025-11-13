package com.landingapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Map;

@Data
@AllArgsConstructor
public class ApiResponse {
    private String message;
    private Map<String, String> errors;

    public ApiResponse(String message) {
        this.message = message;
        this.errors = null;
    }
}
