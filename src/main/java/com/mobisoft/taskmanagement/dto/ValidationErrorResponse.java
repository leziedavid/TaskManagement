package com.mobisoft.taskmanagement.dto;
import java.time.LocalDateTime;
import java.util.HashMap;

public record ValidationErrorResponse (
        LocalDateTime timesTamp,
        int htppStatusCode,
        String errorMessage,
        HashMap<String,String> FieldError
    ){}
