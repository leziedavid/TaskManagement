package com.mobisoft.taskmanagement.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ActionDTO {
    
    private Long actionId;
    @NotBlank(message = "La description ne peut pas être vide ou nul")
    private String description;

    @NotBlank(message = "Le nombre hours ne peut pas être vide ou nul")
    private Integer hours;

    private Long taskId;
    private Long userId;
}
