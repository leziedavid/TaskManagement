package com.mobisoft.taskmanagement.dto;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ObservationDTO {
    // private Long observationId;
    private Long observationId;
    @NotBlank(message = "Le libelle ne peut pas être vide ou nul")
    private String libelle;
    @NotBlank(message = "La description ne peut pas être vide ou nul")
    private String description;
    private String ObservationCreatedAt;
    private String ObservationUpdatedAt;
    private Long taskId;
    private Long userId;

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

    private Set<FilesDTO> filesData;  // Liste des fichiers associés

}
