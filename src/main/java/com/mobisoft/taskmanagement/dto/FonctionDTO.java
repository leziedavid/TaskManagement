package com.mobisoft.taskmanagement.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FonctionDTO {

    private Long fonctionId;
    @NotBlank(message = "Le nom de la fonction ne peut pas être vide ou nul")
    private String nonFonction;
    private OffsetDateTime createDate;
    private OffsetDateTime updateDate;
}
