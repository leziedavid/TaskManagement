package com.mobisoft.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "task_actions")
@Data
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskActionId;
    private String description;
    private int hours;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
