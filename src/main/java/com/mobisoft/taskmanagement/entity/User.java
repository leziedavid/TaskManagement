package com.mobisoft.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.OffsetDateTime;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String lastname;
    private String firstname;
    private String phone;
    private String email;
    private String username;
    private String password;
    private String fonction;

    @Enumerated(EnumType.STRING)
    private Gender genre;

    private OffsetDateTime usersCreatedAt = OffsetDateTime.now();
    private OffsetDateTime usersUpdatedAt = OffsetDateTime.now();

    // @ManyToOne
    // @JoinColumn(name = "group_id")
    // private Group group;
    
    // @ManyToMany
    // @JoinColumn(name = "project_id")
    // private Project Project;

}
