package com.mobisoft.taskmanagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobisoft.taskmanagement.dto.AuthDTO;
import com.mobisoft.taskmanagement.dto.BaseResponse;
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


    @PostMapping("/send-otp")
    public ResponseEntity<BaseResponse<AuthDTO>> sendOTPByEmail(@RequestBody AuthDTO data) throws MessagingException{
            AuthDTO authDTO = authServices.sendOTPByEmail(data);
            BaseResponse<AuthDTO> response = new BaseResponse<>(201,authDTO.getMessage(), authDTO);
            return ResponseEntity.ok(response);
    }

    @PostMapping("/ResetPassword")
    public ResponseEntity<BaseResponse<AuthDTO>> ResetPassword(@RequestBody AuthDTO data) throws MessagingException{
            AuthDTO authDTO = authServices.ResetPassword(data);
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
    public ResponseEntity<BaseResponse<AuthDTO>> logout() {
        AuthDTO authDTO = authServices.logout();
        BaseResponse<AuthDTO> response = new BaseResponse<>(200, "Déconnexion réussie", authDTO);
        return ResponseEntity.ok(response);
    }

}
