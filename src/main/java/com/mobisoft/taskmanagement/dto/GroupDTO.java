package com.mobisoft.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class GroupDTO {
    // private Long groupId;
    @NotBlank(message = "Le nom du groupe ne peut pas être vide ou nul")
    private String groupName;

    // private OffsetDateTime groupCreatedAt = OffsetDateTime.now();
    // private OffsetDateTime groupUpdatedAt = OffsetDateTime.now();
}
