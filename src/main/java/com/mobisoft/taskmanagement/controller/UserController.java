package com.mobisoft.taskmanagement.controller;

import com.mobisoft.taskmanagement.dto.UserDTO;
// import com.mobisoft.taskmanagement.utils.HttpRequestStatus;
import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<BaseResponse<UserDTO>> createUser(@Validated @RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        BaseResponse<UserDTO> response = new BaseResponse<>(201, "Utilisateur créé avec succès", createdUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/getAllUsers")
    public ResponseEntity<BaseResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        BaseResponse<List<UserDTO>> response = new BaseResponse<>(200, "Liste des utilisateurs", users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<UserDTO>> updateUser(@PathVariable Long id, @Validated @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO);
        BaseResponse<UserDTO> response = new BaseResponse<>(200, "Utilisateur mis à jour avec succès", updatedUser);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Utilisateur supprimé avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
