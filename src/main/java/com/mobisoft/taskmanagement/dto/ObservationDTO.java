package com.mobisoft.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ObservationDTO {
    // private Long observationId;
    @NotBlank(message = "Le libelle ne peut pas être vide ou nul")
    private String libelle;
    @NotBlank(message = "La description ne peut pas être vide ou nul")
    private String description;
    private Long taskId;
    private Long userId;
}
