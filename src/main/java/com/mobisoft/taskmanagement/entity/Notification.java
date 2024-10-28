package com.mobisoft.taskmanagement.entity;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "notifications")
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private String title;
    private String message;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "statut_lecteur", columnDefinition = "integer default 0")
    private Integer statutLecteur = 0;

    @Column(name = "entity_id")
    private Long entityId; // Ajouté pour représenter l'identifiant de l'entité

    @Column(name = "entity_type")
    private String entityType; // Ajouté pour représenter le type d'entité

    @Column(name = "project_Id")
    private Long projectId; // Ajouté pour représenter le type d'entité

    @ManyToOne
    @JoinColumn(name = "addBy")
    private User user;

    @ManyToMany
    @JoinTable(
        name = "user_notifications",
        joinColumns = @JoinColumn(name = "notification_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    @OneToMany(mappedBy = "notification")
    private Set<NotificationEntity> entities = new HashSet<>();
}
