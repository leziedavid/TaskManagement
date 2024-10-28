package com.mobisoft.taskmanagement.dto;


import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mobisoft.taskmanagement.entity.Priority;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.validation.annotation.ValidDate;
import com.mobisoft.taskmanagement.validation.annotation.ValidEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

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

    private String prioColor;
    private String stateColor;
    private int progress;
    
    private String projectCreatedAt;
    private String projectUpdatedAt;

    private String projectDescription;
    private String projectCodes;
    private String projectNombreJours;

    @ValidDate(message = "La date de début ne peut pas être nulle")
    private String projectStartDate;

    @ValidDate(message = "La date de fin ne peut pas être nulle")
    private String projectEndDate;
    // @NotNull(message = "L'ID de l'utilisateur ne peut pas être nul")
    private Long userId;

    // liste des utilisateur affecter au projet crée
    private String users;
    // liste des fichier ajouter au projet

    private MultipartFile fichiers1;
    private MultipartFile fichiers2;
    private MultipartFile fichiers3;
    private MultipartFile fichiers4;
    private MultipartFile fichiers5;

    private String title1;
    private String title2;
    private String title3;
    private String title4;
    private String title5;
    
    // nombre de fichier envoyer
    private String nbfiles;

}
