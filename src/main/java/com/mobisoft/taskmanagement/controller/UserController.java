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
import org.springframework.web.bind.annotation.RestController;

import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.dto.UserDTO;
import com.mobisoft.taskmanagement.repository.UserRepository;
import com.mobisoft.taskmanagement.service.UserService;
@RestController
@Validated
@RequestMapping("/api/v1")
public class UserController {
    
    @Autowired
    private UserService userService;

        @Autowired
    private UserRepository userRepository;


    @PostMapping("/saveusers")
    public ResponseEntity<BaseResponse<String>> createUser(@Validated @RequestBody UserDTO userDTO) {

        if (userRepository.existsByEmail(userDTO.getEmail())) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new BaseResponse<>(400, "Cet email est déjà associé à un compte !", ""));
        }
        userService.createUser(userDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new BaseResponse<>(201, "compte créé avec succès", ""));
    }
    

    @GetMapping("/getAllUsers")
    public ResponseEntity<BaseResponse<List<UserDTO>>> getAllUsers() {
        List<UserDTO> users = userService.findAllUsers();
        BaseResponse<List<UserDTO>> response = new BaseResponse<>(HttpStatus.OK.value(), "Liste des utilisateurs", users);
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
