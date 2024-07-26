package com.mobisoft.taskmanagement.controller.advice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.web.firewall.RequestRejectedException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.mobisoft.taskmanagement.dto.BaseResponse;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice

public class GlobalExceptionHandler {

     // Gestion des erreurs de validation...
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {

    HashMap<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
    });

    @SuppressWarnings({ "rawtypes", "unchecked" })

    BaseResponse response = new BaseResponse(
            HttpStatus.BAD_REQUEST.value(),
            "There are validation errors.",
            errors);
    return ResponseEntity.badRequest().body(response);
    }

    // Gestion des erreurs de lecture de message HTTP...
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

        @SuppressWarnings({ "rawtypes", "unchecked" })
        BaseResponse response = new BaseResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Le corps de la requête n'est pas lisible.",
                errors);
        return ResponseEntity.badRequest().body(response);
    }


     // Gestion des erreurs d'entité non trouvée...
@ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        BaseResponse<Object> response = new BaseResponse<>(HttpStatus.NOT_FOUND.value(), "Entité introuvable", null);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


    // @ExceptionHandler(RequestRejectedException.class)
    // public ResponseEntity<Object> handleRequestRejectedException(RequestRejectedException ex) {
    //     log.error("Exception non gérée interceptée: {}", ex.getMessage(), ex);
    //     BaseResponse<Object> response = new BaseResponse<>(HttpStatus.NOT_FOUND.value(), "Requête rejetée :" + ex.getMessage(), null);
    //     return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    // }

     // Gestion des autres exceptions non spécifiques...
        @ExceptionHandler(value = {Exception.class})
        public ResponseEntity<Object> handleAllOtherExceptions(Exception ex, WebRequest request) {
            log.error("Exception non gérée interceptée: {} , Classe d'exception: {}", ex.getLocalizedMessage(), ex.getClass().toGenericString());

            Map<String, String> errors = new HashMap<>();
            errors.put("message", ex.getMessage());

            BaseResponse<Object> response = new BaseResponse<>(
                    HttpStatus.BAD_REQUEST.value(),
                    "Bad Request", errors);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }


        

}
