package com.mobisoft.taskmanagement.entity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "departments") // Nom de la table dans la base de données
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long departmentId;
    private String departmentName;
    private String departmentSigle;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime departmentCreatedAt = OffsetDateTime.now();
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    private OffsetDateTime departmentUpdatedAt = OffsetDateTime.now();

    // @ManyToMany
    // @JoinTable(
    //     name = "department_users",
    //     joinColumns = @JoinColumn(name = "department_id"),
    //     inverseJoinColumns = @JoinColumn(name = "user_id")
    // )
    // private Set<User> users;

    @ManyToMany(mappedBy = "departments")
    @JsonIgnore
    private List<User> users;

    

    @ManyToMany
    @JoinTable(
        name = "department_projects", // Nom de la table de jointure pour les projets
        joinColumns = @JoinColumn(name = "department_id"),
        inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects;
}
