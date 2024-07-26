package com.mobisoft.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ActionDTO {
    
    private Long actionId;
    @NotBlank(message = "La description ne peut pas être vide ou nul")
    private String description;

    // @NotBlank(message = "Le nombre hours ne peut pas être vide ou nul")
    // @NotEmpty(message = "Le nombre hours ne peut pas être vide ou nul")
    private Integer hours;

    private Long taskId;
    private Long userId;
}
