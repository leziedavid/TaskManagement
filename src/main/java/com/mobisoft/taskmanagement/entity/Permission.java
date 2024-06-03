package com.mobisoft.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.Data;

// import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Data
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permissionId;

    private String permissionName;

    private OffsetDateTime permissionCreatedAt = OffsetDateTime.now();
    private OffsetDateTime permissionUpdatedAt = OffsetDateTime.now();

    @ManyToMany(mappedBy = "permissions")
    private Set<Group> groups;

    @ManyToMany
    @JoinTable(
        name = "users_permissions",
        joinColumns = @JoinColumn(name = "permission_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users;
}
