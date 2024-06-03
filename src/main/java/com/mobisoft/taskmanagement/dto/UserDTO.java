package com.mobisoft.taskmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import com.mobisoft.taskmanagement.entity.Gender;
// import jakarta.validation.constraints.NotNull;
import com.mobisoft.taskmanagement.validation.annotation.ValidEnum;
// import java.time.LocalDate;

@Data
public class UserDTO {
    // private Long userId;

    @NotBlank(message = "Le nom ne peut pas être vide ou nul")
    private String lastname;

    @NotBlank(message = "Le prénom ne peut pas être vide ou nul")
    private String firstname;

    @NotBlank(message = "L'email ne peut pas être vide ou nul")
    private String email;

    @NotBlank(message = "Le numero ne peut pas être vide ou nul")
    private String phone;

    @NotBlank(message = "Le nom d'utilisateur ne peut pas être vide ou nul")
    private String username;

    @NotBlank(message = "Le mot de passe ne peut pas être vide ou nul")
    private String password;

    @NotBlank(message = "La fonction ne peut pas être vide ou nul")
    private String fonction;

    @ValidEnum(enumClass = Gender.class, message = "Le genre ne peut pas être vide ou nul")
    // @NotNull(message = "Le genre ne peut pas être vide ou nul")
    private String genre;

    // private OffsetDateTime usersCreatedAt = OffsetDateTime.now();
    // private OffsetDateTime usersUpdatedAt = OffsetDateTime.now();

}
