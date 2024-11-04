package com.mobisoft.taskmanagement.dto;

import java.time.LocalDateTime;
import java.util.List;

public class AbonnementDTO {
    private Long abonnementId;
    private Long idTask;
    private String email;
    private String name;
    private List<Long> userIds; // Champ pour les IDs des utilisateurs
    private LocalDateTime createdAt; // Champ pour la date de création
    private Long userId; // Champ pour l'ID de l'utilisateur

    // Getters et Setters
    public Long getAbonnementId() {
        return abonnementId;
    }

    public void setAbonnementId(Long abonnementId) {
        this.abonnementId = abonnementId;
    }

    public Long getIdTask() {
        return idTask;
    }

    public void setIdTask(Long idTask) {
        this.idTask = idTask;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public List<Long> getUserIds() {
        return userIds; // Getter pour userIds
    }

    public void setUserIds(List<Long> userIds) {
        this.userIds = userIds; // Setter pour userIds
    }

        public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt; // Getter pour createdAt
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt; // Setter pour createdAt
    }
}
