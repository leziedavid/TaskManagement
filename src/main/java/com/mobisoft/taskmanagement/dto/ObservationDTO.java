package com.mobisoft.taskmanagement.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ObservationDTO {
    private Long observationId;
    @NotBlank(message = "Le libelle ne peut pas être vide ou nul")
    private String libelle;
    @NotBlank(message = "La description ne peut pas être vide ou nul")
    private String description;
    private String file;
    private Long taskId;
    private Long userId;
}
