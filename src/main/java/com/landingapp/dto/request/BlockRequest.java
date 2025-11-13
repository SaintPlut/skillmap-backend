package com.landingapp.dto.request;

import lombok.Data;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class BlockRequest {

    @NotBlank(message = "ID блока обязателен")
    private String blockId;

    @NotBlank(message = "Тип блока обязателен")
    @Pattern(regexp = "header|text|image|footer", message = "Неверный тип блока")
    private String type;

    private String content;

    @NotNull(message = "Порядок блока обязателен")
    @Min(value = 0, message = "Порядок не может быть отрицательным")
    private Integer order;
}
