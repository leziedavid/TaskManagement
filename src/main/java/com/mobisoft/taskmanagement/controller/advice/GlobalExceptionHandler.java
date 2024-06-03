package com.mobisoft.taskmanagement.controller.advice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.mobisoft.taskmanagement.dto.BaseErrorResponse;
import com.mobisoft.taskmanagement.dto.BaseResponse;

import jakarta.persistence.EntityNotFoundException;
// import com.mobisoft.taskmanagement.dto.ValidationErrorResponse;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;

@Slf4j
@RestControllerAdvice

public class GlobalExceptionHandler {

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {

    HashMap<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
    });
    BaseResponse response = new BaseResponse(
            HttpStatus.BAD_REQUEST.value(),
            "There are validation errors.",
            errors);
    return ResponseEntity.badRequest().body(response);
    }

@ExceptionHandler(HttpMessageNotReadableException.class)
public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {

        HashMap<String, String> errors = new HashMap<>();
        if(ex.getCause() instanceof JsonMappingException) {
            JsonMappingException jme = (JsonMappingException) ex.getCause();
            // Récupérer le nom du champ de l'entité
            String fieldName = jme.getPath().stream()
                .map(JsonMappingException.Reference::getFieldName)
                .collect(Collectors.joining("."));
                
            String errorMessage = jme.getOriginalMessage();
            errors.put(fieldName, errorMessage);

        } else {
            errors.put("message", ex.getMessage());
        }

        BaseResponse response = new BaseResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Le corps de la requête n'est pas lisible.",
                errors);
        return ResponseEntity.badRequest().body(response);
    }


@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        BaseResponse<Object> response = new BaseResponse<>(HttpStatus.NOT_FOUND.value(), "Entity Not Found", errors);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


@ExceptionHandler(value = {Exception.class})
public ResponseEntity<Object> handleAllOtherExceptions(Exception ex, WebRequest request) {
        log.error("Unhandled exception caught: {} , exceptionClass: {}", ex.getLocalizedMessage(), ex.getClass().toGenericString());

        BaseErrorResponse response = new BaseErrorResponse(
            LocalDateTime.now(),
            ex.getLocalizedMessage(),
            HttpStatus.BAD_REQUEST.value(),
            HttpStatus.BAD_REQUEST,
            request.getDescription(false).replace("uri=", ""));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
