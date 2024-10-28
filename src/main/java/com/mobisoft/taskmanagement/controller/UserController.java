package com.mobisoft.taskmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.dto.UserDTO;
import com.mobisoft.taskmanagement.dto.UserDepartmentDTO;
import com.mobisoft.taskmanagement.dto.UsersResponse;
import com.mobisoft.taskmanagement.entity.Role;
import com.mobisoft.taskmanagement.repository.UserRepository;
import com.mobisoft.taskmanagement.service.UserService;

import jakarta.ws.rs.core.MediaType;

@RestController
@Validated
@RequestMapping("/api/v1")
public class UserController {
    
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

        @PostMapping(value = "/users/addUsers", consumes = MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity<BaseResponse<UserDTO>> addUsers(
            @RequestParam("lastname") String lastname,
            @RequestParam("firstname") String firstname,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("fonction") String fonction,
            @RequestParam("genre") String genre,
            @RequestParam("role") Role role,
            @RequestParam("departmentId") Long departmentId,
            @RequestPart(value = "profil", required = false) MultipartFile profil) {
    
        UserDTO userDTO = new UserDTO();
        userDTO.setLastname(lastname);
        userDTO.setFirstname(firstname);
        userDTO.setEmail(email);
        userDTO.setPhone(phone);
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setFonction(fonction);
        userDTO.setGenre(genre);
        userDTO.setRole(role);
        userDTO.setDepartmentId(departmentId);
    
        UserDTO createdUser = userService.addUsers(userDTO, profil);
        BaseResponse<UserDTO> response = new BaseResponse<>(HttpStatus.CREATED.value(), "Compte créé avec succès", createdUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(value = "/users/updateUser/{userId}", consumes = MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity<BaseResponse<UserDTO>> updateUser(
            @PathVariable Long userId,
            @RequestParam("lastname") String lastname,
            @RequestParam("firstname") String firstname,
            @RequestParam("email") String email,
            @RequestParam("phone") String phone,
            @RequestParam("username") String username,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam("fonction") String fonction,
            @RequestParam("genre") String genre,
            @RequestParam("role") Role role,
            @RequestParam("departmentId") Long departmentId,
            @RequestPart(value = "profil", required = false) MultipartFile profil) {

        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setLastname(lastname);
        userDTO.setFirstname(firstname);
        userDTO.setEmail(email);
        userDTO.setPhone(phone);
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setFonction(fonction);
        userDTO.setGenre(genre);
        userDTO.setRole(role);
        userDTO.setDepartmentId(departmentId);

        UserDTO updatedUser = userService.updateUser(userId, userDTO, profil);
        BaseResponse<UserDTO> response = new BaseResponse<>(HttpStatus.OK.value(), "Compte mis à jour avec succès", updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/users/getAllUsers")
    public ResponseEntity<BaseResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        BaseResponse<List<UserDTO>> response = new BaseResponse<>(HttpStatus.OK.value(), "Liste des utilisateurs", users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/users/getAllUsersByDepartment2")
    public ResponseEntity<BaseResponse<UsersResponse>> getAllUsersByDepartment2(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "userId") String sortBy) {
        try {
            // Appeler le service pour obtenir la liste paginée des utilisateurs avec leurs départements
            UsersResponse usersWithDepartments = userService.getAllUsersWithDepartments2(page, size, sortBy);

            // Créer une réponse de base avec les utilisateurs
            BaseResponse<UsersResponse> response = new BaseResponse<>(
                    HttpStatus.OK.value(),
                    "Liste des utilisateurs avec leurs départements",
                    usersWithDepartments
            );

            // Retourner la réponse encapsulée dans un ResponseEntity
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // En cas d'erreur, retourner une réponse d'erreur
            BaseResponse<UsersResponse> errorResponse = new BaseResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Erreur lors de la récupération des utilisateurs : " + e.getMessage(),
                    null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/users/getAllUsersByDepartment")
    public ResponseEntity<BaseResponse<List<UserDepartmentDTO>>> getAllUsersByDepartment() {
        try {
            // Appeler le service pour obtenir la liste des utilisateurs avec leurs départements
            List<UserDepartmentDTO> usersWithDepartments = userService.getAllUsersWithDepartments();

            // Créer une réponse de base avec les utilisateurs
            BaseResponse<List<UserDepartmentDTO>> response = new BaseResponse<>(
                    HttpStatus.OK.value(),
                    "Liste des utilisateurs avec leurs départements",
                    usersWithDepartments
            );

            // Retourner la réponse encapsulée dans un ResponseEntity
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // En cas d'erreur, retourner une réponse d'erreur
            BaseResponse<List<UserDepartmentDTO>> errorResponse = new BaseResponse<>(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Erreur lors de la récupération des utilisateurs : " + e.getMessage(),
                    null
            );
            return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<BaseResponse<UserDTO>> updateUser(@PathVariable Long id, @Validated @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        BaseResponse<UserDTO> response = new BaseResponse<>(200, "Utilisateur mis à jour avec succès", updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Utilisateur supprimé avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/users/update-usersRoles")
    public ResponseEntity<BaseResponse<UserDTO>> updateUserStatus(@RequestParam Long userId,@RequestParam Integer isValides) {
        UserDTO updateStatus = userService.updateUserStatus(userId, isValides);
        BaseResponse<UserDTO> response = new BaseResponse<>(200, "Statut de l'action mis à jour avec succès", updateStatus);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/users/getUserByIdWithDepartments/{userId}")
    public ResponseEntity<BaseResponse<UserDepartmentDTO>> getUserByIdWithDepartments(@PathVariable Long userId) {

        try {

            UserDepartmentDTO user = userService.getUserByIdWithDepartments(userId);
            BaseResponse<UserDepartmentDTO> response = new BaseResponse<>(HttpStatus.OK.value(), "Utilisateur trouvé", user);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            // Gestion d'exception pour utilisateur non trouvé
            BaseResponse<UserDepartmentDTO> response = new BaseResponse<>(HttpStatus.NOT_FOUND.value(), e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


}
