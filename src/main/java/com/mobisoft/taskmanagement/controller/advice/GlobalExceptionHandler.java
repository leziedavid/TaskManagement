package com.mobisoft.taskmanagement.controller.advice;

import java.util.HashMap;
import java.util.Map;
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
import com.mobisoft.taskmanagement.dto.BaseResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;

import com.mobisoft.taskmanagement.exception.EmailAlreadyExistsException; // Import de l'exception personnalisée

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


@ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", ex.getMessage());
    BaseResponse<Object> response = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "Argument invalide", errors);
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}



    // Gestion de l'exception EmailAlreadyExistsException...
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Object> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        BaseResponse<Map<String, String>> response = new BaseResponse<>(
                HttpStatus.CONFLICT.value(), // Utilise 409 Conflict pour les erreurs de duplication
                "L'email est déjà utilisé.",
                errors);
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


@ExceptionHandler(PersistenceException.class)
    public ResponseEntity<Object> handlePersistenceException(PersistenceException ex) {
    Map<String, String> errors = new HashMap<>();
    errors.put("message", ex.getMessage());
    BaseResponse<Object> response = new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erreur de persistance des données", errors);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
}

@ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleIllegalStateException(IllegalStateException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", ex.getMessage());
        BaseResponse<Object> response = new BaseResponse<>(HttpStatus.BAD_REQUEST.value(), "État illégal", errors);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
