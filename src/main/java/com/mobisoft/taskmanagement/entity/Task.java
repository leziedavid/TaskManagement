package com.mobisoft.taskmanagement.entity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private String taskCode;

    private String taskName;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String taskDescription;

    @Enumerated(EnumType.STRING)
    private Priority taskPriority;

    @Enumerated(EnumType.STRING)
    private State taskState;

    private String prioColor;
    private String stateColor;

    private String taskNombreJours;
    private String taskNombreHeurs;
    private String difficulte;
    @Column(name = "level", columnDefinition = "integer default 0")
    private Integer level= 0;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime taskStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime taskEndDate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime taskCreatedAt = OffsetDateTime.now();
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime taskUpdatedAt = OffsetDateTime.now();

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime alerteDate;

    @Min(0)
    @Max(100)
    @Column(name = "progression", columnDefinition = "integer default 0")
    private Integer progression;
    
    private String colorCode;
    
    @Column(name = "is_valides", columnDefinition = "integer default 0")
    private Integer isValides = 0;
    
    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "assigned_user_id")
    private User assigned;


    // Méthode pour obtenir l'ID du projet
    public Long getProjectId() {
        if (project != null) {
            return project.getProjectId();
        }
        return null;
    }

}
