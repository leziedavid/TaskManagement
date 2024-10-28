// package com.mobisoft.taskmanagement.dto;

// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
// import com.fasterxml.jackson.annotation.JsonInclude;
// import com.mobisoft.taskmanagement.entity.Priority;
// import com.mobisoft.taskmanagement.entity.State;
// import com.mobisoft.taskmanagement.validation.annotation.ValidEnum;

// import jakarta.validation.constraints.NotBlank;
// import jakarta.validation.constraints.NotNull;
// import lombok.Data;

// @Data
// @JsonInclude(JsonInclude.Include.NON_NULL)
// @JsonIgnoreProperties(ignoreUnknown = true)
// public class TaskDTO2 {
//     private Long taskId;

//     @NotBlank(message = "Le nom de la tâche ne peut pas être vide ou nul")
//     private String taskName;

//     private String taskCode;

//     @NotBlank(message = "La description ne peut pas être vide ou nulle")
//     private String taskDescription;

//     // @NotNull(message = "La priorité de la tâche ne peut pas être nulle")
//     @ValidEnum(enumClass = Priority.class, message = "La priorité de la tâche ne peut pas être nulle")
//     private String taskPriority;

//     // @NotNull(message = "L'état de la tâche ne peut pas être nul")
//     @ValidEnum(enumClass = State.class, message = "La priorité de la tâche ne peut pas être nulle")
//     private String taskState;

//     private String prioColor;
//     private String stateColor;

//     private String taskNombreJours;
//     private String taskNombreHeurs;
//     private String projectCodes;
//     private Integer progression;


//     // @ValidDate(message = "La date de debut ne peut pas être nulle")
//     private String taskStartDate;

//     // @ValidDate(message = "La date de fin ne peut pas être nulle")
//     private String taskEndDate;

//     private String taskCreatedAt;
//     private String taskUpdatedAt;
//     private Integer isValides;
//     private String alerteDate;


//     // @NotNull(message = "L'ID du projet ne peut pas être nul")
//     private Long projectId;

//     // @NotNull(message = "Le code du projet ne peut pas être nul")
//     private String colorCode;

//     @NotNull(message = "L'ID de l'utilisateur ne peut pas être nul")
//     private Long userId;

//     @NotNull(message = "L'ID de attribué  a ne peut pas être nul")
//     private Long assigned;


//     public Long getProjectId() {
//         return projectId;
//     }

//     public void setProjectId(Long projectId) {
//         this.projectId = projectId;
//     }

// }
