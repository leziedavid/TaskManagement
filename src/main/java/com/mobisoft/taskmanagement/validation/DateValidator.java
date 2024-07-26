package com.mobisoft.taskmanagement.validation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.mobisoft.taskmanagement.validation.annotation.ValidDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateValidator implements ConstraintValidator<ValidDate, String> {
    private String pattern;

    @Override
    public void initialize(ValidDate constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false;
        }
        try {
            LocalDate date = LocalDate.parse(value, DateTimeFormatter.ofPattern(pattern));
            return !date.isAfter(LocalDate.now());
            
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}