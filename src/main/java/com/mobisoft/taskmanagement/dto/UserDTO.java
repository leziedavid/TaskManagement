package com.mobisoft.taskmanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mobisoft.taskmanagement.entity.Gender;
import com.mobisoft.taskmanagement.entity.Role; // Importer l'énumération Role
import com.mobisoft.taskmanagement.validation.annotation.ValidEnum;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDTO {
    private Long userId;
    
    @NotBlank(message = "Le nom ne peut pas être vide ou nul")
    private String lastname;

    @NotBlank(message = "Le prénom ne peut pas être vide ou nul")
    private String firstname;

    @NotBlank(message = "L'email ne peut pas être vide ou nul")
    private String email;

    @NotBlank(message = "Le numéro ne peut pas être vide ou nul")
    private String phone;

    @NotBlank(message = "Le nom d'utilisateur ne peut pas être vide ou nul")
    private String username;

    @NotBlank(message = "Le mot de passe ne peut pas être vide ou nul")
    private String password;

    @NotBlank(message = "La fonction ne peut pas être vide ou nul")
    private String fonction;

    private String response;
    private String token;

    @ValidEnum(enumClass = Gender.class, message = "Le genre ne peut pas être vide ou nul")
    private String genre;

    private Integer isValides;

    @ValidEnum(enumClass = Role.class, message = "Le rôle est invalide") // Validation pour le rôle
    private Role role; // Champ pour le rôle


    @NotBlank(message = "Le département ne peut pas être vide ou nul")
    private String departmentName;
    private Long departmentId;
    
    private String profil;

}
