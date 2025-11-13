package com.landingapp.dto.request;

import lombok.Data;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
public class UpdateLandingRequest {

    @NotBlank(message = "Название обязательно")
    @Size(min = 1, max = 255, message = "Название должно быть от 1 до 255 символов")
    private String name;

    @NotBlank(message = "ID шаблона обязателен")
    private String templateId;

    @Valid
    @NotEmpty(message = "Должен быть хотя бы один блок")
    private List<BlockRequest> blocks;
}
