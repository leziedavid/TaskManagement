package com.mobisoft.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
// import java.util.Set;

@Entity
@Table(name = "projects")
@Data
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    private String projectName;

    @Enumerated(EnumType.STRING)
    private State projectState;

    @Enumerated(EnumType.STRING)
    private Priority projectPriority;

    private String projectDescription;

    private LocalDate projectStartDate;
    private LocalDate projectEndDate;

    private OffsetDateTime projectCreatedAt = OffsetDateTime.now();
    private OffsetDateTime projectUpdatedAt = OffsetDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    

}
