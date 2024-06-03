package com.mobisoft.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;
// import io.micrometer.observation.Observation;

@Entity
@Table(name = "tasks")
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;

    private String taskName;
    private String taskDescription;

    @Enumerated(EnumType.STRING)
    private Priority taskPriority;

    @Enumerated(EnumType.STRING)
    private State taskState;

    private LocalDate taskStartDate;
    private LocalDate taskEndDate;

    private OffsetDateTime taskCreatedAt = OffsetDateTime.now();
    private OffsetDateTime taskUpdatedAt = OffsetDateTime.now();

    private Integer progression;
    private String colorCode;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
        name = "task_actions",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "actions_id")
    )
    private Set<Permission> Action;

    @ManyToMany
    @JoinTable(
        name = "task_observations",
        joinColumns = @JoinColumn(name = "task_id"),
        inverseJoinColumns = @JoinColumn(name = "observations_id")
    )
    private Set<Permission> Observations;

}
