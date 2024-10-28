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
    private String libelle;
    private String description;
    private String NombreJours;
    private String actionCreatedAt;
    private String actionUpdatedAt;
    private Integer hours;
    private Long taskId;
    private Long userId;
    private String actionStartDate;
    private String actionEndDate;
    private Integer isValides;
}
