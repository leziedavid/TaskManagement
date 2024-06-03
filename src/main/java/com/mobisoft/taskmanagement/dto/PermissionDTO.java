package com.mobisoft.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

// import java.time.LocalDate;
// import java.time.OffsetDateTime;

@Data
public class PermissionDTO {
    // private Long permissionId;
    @NotBlank(message = "Le nom de la permission ne peut pas être vide ou nul")
    private String permissionName;

    // private OffsetDateTime permissionCreatedAt = OffsetDateTime.now();
    // private OffsetDateTime permissionUpdatedAt = OffsetDateTime.now();
}
