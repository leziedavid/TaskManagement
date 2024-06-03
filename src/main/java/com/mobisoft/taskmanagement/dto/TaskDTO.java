package com.mobisoft.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

// import com.mobisoft.taskmanagement.entity.Gender;
// import java.time.LocalDate;
// import org.springframework.format.annotation.DateTimeFormat;

import com.mobisoft.taskmanagement.entity.Priority;
import com.mobisoft.taskmanagement.entity.State;

import com.mobisoft.taskmanagement.validation.annotation.ValidDate;
import com.mobisoft.taskmanagement.validation.annotation.ValidEnum;

@Data
public class TaskDTO {
    private Long taskId;

    @NotBlank(message = "Le nom de la tâche ne peut pas être vide ou nul")
    private String taskName;

    @NotBlank(message = "La description ne peut pas être vide ou nulle")
    private String taskDescription;

    // @NotNull(message = "La priorité de la tâche ne peut pas être nulle")
    @ValidEnum(enumClass = Priority.class, message = "La priorité de la tâche ne peut pas être nulle")
    private String taskPriority;

    // @NotNull(message = "L'état de la tâche ne peut pas être nul")
    @ValidEnum(enumClass = State.class, message = "La priorité de la tâche ne peut pas être nulle")
    private String taskState;

    @ValidDate(message = "Format de date non valide, format requis: yyyy-MM-dd")
    private String taskStartDate;

    @ValidDate(message = "Format de date non valide, format requis: yyyy-MM-dd")
    private String taskEndDate;

    @NotNull(message = "L'ID du projet ne peut pas être nul")
    private Long projectId;

    @NotNull(message = "L'ID de l'utilisateur ne peut pas être nul")
    private Long userId;
}
