package com.mobisoft.taskmanagement.dto;

import org.springframework.http.HttpStatusCode;
// import lombok.AllArgsConstructor;
// import lombok.Data;
// import java.util.HashMap;
import java.time.LocalDateTime;


public record  BaseErrorResponse (
    LocalDateTime timesTamp,
    String errorMessage,
    int htppStatusCode,
    HttpStatusCode httpStatusMessage,
    String requestPath
    ){}
