package com.mobisoft.taskmanagement.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mobisoft.taskmanagement.entity.User;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class AuthDTO {

    private int status;
    private String error;
    private String message;
    private String token;
    private String refreshToken;
    private String expirationTime;
    private String username;
    private String fonction;
    private String email;
    private int otp;
    private String password;
    
    // private User ourUsers;
    // private List<User> ourUsersList;
    // public void setUser(User user) {
    //     throw new UnsupportedOperationException("Unimplemented method 'setUser'");
    // }
    // public void setUserList(List<User> result) {
    //     throw new UnsupportedOperationException("Unimplemented method 'setUserList'");
    // }

}

