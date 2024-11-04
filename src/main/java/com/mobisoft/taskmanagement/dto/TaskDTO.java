package com.mobisoft.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mobisoft.taskmanagement.entity.Priority;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.validation.annotation.ValidEnum;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaskDTO {
    private Long taskId;

    @NotBlank(message = "Le nom de la tâche ne peut pas être vide ou nul")
    private String taskName;

    private String taskCode;

    @NotBlank(message = "La description ne peut pas être vide ou nulle")
    private String taskDescription;

    @ValidEnum(enumClass = Priority.class, message = "La priorité de la tâche ne peut pas être nulle")
    private String taskPriority;

    @ValidEnum(enumClass = State.class, message = "L'état de la tâche ne peut pas être nul")
    private String taskState;

    private String prioColor;
    private String stateColor;

    private String taskNombreJours;
    private String taskNombreHeurs;
    private String projectCodes;
    private Integer progression;

    private String taskStartDate;
    private String taskEndDate;
    private String taskCreatedAt;
    private String taskUpdatedAt;
    private Integer isValides;
    private String alerteDate;

    private String difficulte;
    private Integer level;

    private Long projectId;  // Vous pouvez garder cela si nécessaire pour l'ID du projet
    private String colorCode;

    @NotNull(message = "L'ID de l'utilisateur ne peut pas être nul")
    private Long userId;

    @NotNull(message = "L'ID de l'utilisateur attribué ne peut pas être nul")
    private Long assigned;

    // Ajoutez ces propriétés pour les objets liés
    private ProjectDTO project;   // Pour stocker le ProjectDTO
    private UserDTO user;         // Pour stocker le UserDTO
    private UserDTO assignedUser; // Pour stocker le UserDTO de l'utilisateur assigné
}
