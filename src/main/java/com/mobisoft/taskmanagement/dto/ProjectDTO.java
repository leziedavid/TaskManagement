package com.mobisoft.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;
import com.mobisoft.taskmanagement.entity.Priority;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.validation.annotation.ValidDate;
import com.mobisoft.taskmanagement.validation.annotation.ValidEnum;

import lombok.Data;

// import java.time.LocalDate;
// import java.time.OffsetDateTime;
// import org.springframework.format.annotation.DateTimeFormat;

@Data
public class ProjectDTO {
    private Long projectId;

    @NotBlank(message = "Le nom du projet ne peut pas être vide ou nul")
    private String projectName;

    // @NotNull(message = "L'état du projet ne peut pas être nul")
    @ValidEnum(enumClass = State.class, message = "L'état du projet ne peut pas être nul")
    private String projectState;

    // @NotNull(message = "La priorité du projet ne peut pas être nulle")
    @ValidEnum(enumClass = Priority.class, message = "La priorité du projet ne peut pas être nulle")
    private String projectPriority;

    private String projectDescription;

    // @NotNull(message = "La date de début ne peut pas être nulle")
    // @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ValidDate(message = "La date de début ne peut pas être nulle")
    private String projectStartDate;

    // @NotNull(message = "La date de fin ne peut pas être nulle")
    // @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ValidDate(message = "La date de fin ne peut pas être nulle")
    private String projectEndDate;

}
