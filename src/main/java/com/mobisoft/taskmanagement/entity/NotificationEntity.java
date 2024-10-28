package com.mobisoft.taskmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "notification_entities")
@Data
public class NotificationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type")
    private String entityType; // Type de l'entité (par exemple, "Task", "Project")

    @Column(name = "entity_id")
    private Long entityId; // Identifiant de l'entité

    @ManyToOne
    @JoinColumn(name = "notification_id")
    private Notification notification; // Référence à Notification
}
