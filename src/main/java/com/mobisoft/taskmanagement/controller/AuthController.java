package com.mobisoft.taskmanagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobisoft.taskmanagement.dto.AuthDTO;
import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.dto.TokenDTO;
import com.mobisoft.taskmanagement.dto.UserDTO;
import com.mobisoft.taskmanagement.service.AuthServices;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/v1")

public class AuthController {
    
    @Autowired
    private AuthServices authServices;



    @PostMapping("/auth/login")
    public ResponseEntity<BaseResponse<AuthDTO>> login(@Validated @RequestBody AuthDTO req) {
        System.out.println(req);
        AuthDTO authDTO = authServices.login(req);

        BaseResponse<AuthDTO> response = new BaseResponse<>(HttpStatus.OK.value(), "", authDTO);
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<BaseResponse<AuthDTO>> refreshToken(@Validated @RequestBody AuthDTO req) {
        AuthDTO authDTO = authServices.refreshToken(req);
        BaseResponse<AuthDTO> response = new BaseResponse<>(HttpStatus.OK.value(), "Token rafraîchi avec succès", authDTO);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/sendOtp")
    public ResponseEntity<BaseResponse<AuthDTO>> sendOTPByEmail(@RequestBody AuthDTO data) {
        try {
            AuthDTO authDTO = authServices.sendOTPByEmail(data);
            BaseResponse<AuthDTO> response = new BaseResponse<>(authDTO.getStatus(), authDTO.getMessage(), authDTO);
            return ResponseEntity.ok(response);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erreur d'envoi d'email.", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Une erreur inattendue est survenue.", null));
        }
    }



    // @PostMapping("/sendOtp")
    // public ResponseEntity<BaseResponse<AuthDTO>> sendOTPByEmail(@RequestBody AuthDTO data) throws MessagingException{
    //         AuthDTO authDTO = authServices.sendOTPByEmail(data);
    //         BaseResponse<AuthDTO> response = new BaseResponse<>(authDTO.getStatus(),authDTO.getMessage(), authDTO);
    //         return ResponseEntity.ok(response);
    // }

    @PostMapping("/ResetPassword")
    public ResponseEntity<BaseResponse<AuthDTO>> ResetPassword(@RequestBody AuthDTO data) throws MessagingException{
            AuthDTO authDTO = authServices.resetPassword(data);
            BaseResponse<AuthDTO> response = new BaseResponse<>(authDTO.getStatus(), authDTO.getMessage(),authDTO);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<BaseResponse<AuthDTO>> verifyOTP(@RequestBody  AuthDTO data) {

        try {

            AuthDTO authDTO = authServices.verifyOTP(data);
            System.out.println(authDTO.getStatus());
            BaseResponse<AuthDTO> response = new BaseResponse<>(authDTO.getStatus(), authDTO.getMessage(), authDTO);
            return ResponseEntity.ok(response);

        } catch (Exception e) {

            BaseResponse<AuthDTO> response = new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @DeleteMapping("/auth/logout")
    public ResponseEntity<BaseResponse<AuthDTO>> logout(@RequestBody TokenDTO tokenDTO) {
        AuthDTO authDTO = authServices.logout(tokenDTO.getToken());
        BaseResponse<AuthDTO> response = new BaseResponse<>(authDTO.getStatus(), authDTO.getMessage(), authDTO);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/auth/userid")
    public ResponseEntity<BaseResponse<Long>> getUserIdFromToken(@RequestHeader(name = "Authorization") String authorizationHeader) {

        try {
            // Extract token from Authorization header (assuming "Bearer " prefix)
            String token = authorizationHeader.substring(7); // Remove "Bearer "
            
            // Call service method to get user ID from token
            Long userId = authServices.getUserIdFromToken(token);

            // Prepare success response
            BaseResponse<Long> response = new BaseResponse<>(200, "User ID retrieved successfully", userId);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            
            // Handle exceptions
            BaseResponse<Long> errorResponse = new BaseResponse<>(500, "Échec de la récupération de l'ID utilisateur", null);
            return ResponseEntity.status(500).body(errorResponse);
        }
    }


    /**
     * Endpoint pour récupérer toutes les informations de l'utilisateur à partir du token JWT.
     * Utilise l'en-tête Authorization pour extraire le token JWT.
     *
     * @param authorizationHeader En-tête Authorization avec le token JWT (format "Bearer <token>")
     * @return ResponseEntity<BaseResponse<UserDTO>> Réponse contenant les informations de l'utilisateur
     */
    @GetMapping("/auth/userinfo")
    public ResponseEntity<BaseResponse<UserDTO>> getUserInfoFromToken(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // Extrait le token JWT en enlevant le préfixe "Bearer "
            String token = authorizationHeader.substring(7); // Remove "Bearer "

            // Appelle la méthode du service pour récupérer les informations de l'utilisateur
            UserDTO userInfo = authServices.getUserInfoFromToken(token);

            // Prépare la réponse de succès
            BaseResponse<UserDTO> response = new BaseResponse<>(HttpStatus.OK.value(), "Informations de l'utilisateur récupérées avec succès", userInfo);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // Gère les exceptions et renvoie une réponse d'erreur
            BaseResponse<UserDTO> errorResponse = new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Échec de récupération des informations de l'utilisateur", null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


}
