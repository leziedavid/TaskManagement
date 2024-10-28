package com.mobisoft.taskmanagement.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Entity
@Table(name = "projects")
@Data
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long projectId;

    private String projectName;
    private String projectCodes;
    private String projectNombreJours;

    @Enumerated(EnumType.STRING)
    private State projectState;
    
    @Enumerated(EnumType.STRING)
    private Priority projectPriority;

    private String prioColor;
    private String stateColor;
    @Min(0)
    @Max(100)
    
    @Column(name = "progress", columnDefinition = "integer default 0")
    private int progress;
    @Lob
    @Column(columnDefinition = "TEXT")
    private String projectDescription; // Champ pour le texte long


    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime projectStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime projectEndDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    @Column(name = "project_created_at")
    private OffsetDateTime projectCreatedAt = OffsetDateTime.now();
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime projectUpdatedAt = OffsetDateTime.now();


    @OneToMany
    @JoinTable(name = "project_filesData",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "filesData_id")
    )
    private Set<FilesData> filesData = new HashSet<>(); // Initialisation avec une HashSet
    // private Set<FilesData> filesData;

    @ManyToOne
    @JoinColumn(name = "user_id") // Nom de la colonne dans la table projects faisant référence à l'utilisateur qqui a crée le projet
    private User user;




    // Constructeur par défaut
    public Project() {
        // filesData est initialisé avec une HashSet vide
    }

    
    public Object getId() {
        return projectId;
    }


    public Optional<Project> map(Object object) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'map'");
    }


}
