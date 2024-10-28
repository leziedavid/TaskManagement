package com.mobisoft.taskmanagement.dto;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDTO {
    
    private Long notificationId;

    @NotBlank(message = "Le titre ne peut pas être vide ou nul")
    private String title;

    @NotBlank(message = "Le message ne peut pas être vide ou nul")
    private String message;

    private String createdAt;

    private Long projectId;

    @NotNull(message = "L'identifiant de l'entité ne peut pas être nul")
    private Long entityId;

    @NotBlank(message = "Le type d'entité ne peut pas être vide ou nul")
    private String entityType; // Par exemple : "Task", "Project", etc.

    @NotNull(message = "L'ID de l'utilisateur  par a ne peut pas être nul")
    private Long addBy; // Changez ici pour Long au lieu de UserDTO

    private Set<Long> userIds; // Liste des utilisateurs associés à la notification

    private Set<UserDTO> userAddBBy; // L'utilisateurs qui a generer la notification
    private Set<UserDTO> users;
    private Integer statutLecteur;

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
    
    // Getter et Setter
    public Integer getStatutLecteur() {
        return statutLecteur;
    }

    public void setStatutLecteur(Integer statutLecteur) {
        this.statutLecteur = statutLecteur;
    }

    public Set<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDTO> users) {
        this.users = users;
    }

}
