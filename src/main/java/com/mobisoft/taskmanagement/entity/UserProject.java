package com.mobisoft.taskmanagement.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "user_project")
@Data

public class UserProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userprojectId;

    // @ManyToOne
     @ManyToOne(fetch = FetchType.EAGER) // Chargement Eager
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER) // Chargement Eager
    @JoinColumn(name = "project_id")
    private Project project;

    @Column(columnDefinition = "boolean default false")
    private boolean leader;
}
