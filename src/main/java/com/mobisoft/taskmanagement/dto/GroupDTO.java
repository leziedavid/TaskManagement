package com.mobisoft.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
public class GroupDTO {
    // private Long groupId;
    @NotBlank(message = "Le nom du groupe ne peut pas être vide ou nul")
    private String groupName;

    // private OffsetDateTime groupCreatedAt = OffsetDateTime.now();
    // private OffsetDateTime groupUpdatedAt = OffsetDateTime.now();
}
