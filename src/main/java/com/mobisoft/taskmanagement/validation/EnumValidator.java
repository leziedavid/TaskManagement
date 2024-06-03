package com.mobisoft.taskmanagement.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
// import java.lang.reflect.InvocationTargetException;
// import java.lang.reflect.Method;
import  java.util.Arrays;
import com.mobisoft.taskmanagement.validation.annotation.ValidEnum;

public class EnumValidator implements ConstraintValidator<ValidEnum, CharSequence> {
    
    private ValidEnum validEnum;

    @Override
    public void initialize(ValidEnum constraintAnnotation) {
        this.validEnum = constraintAnnotation;
    }

    @Override
    public boolean isValid(CharSequence value, ConstraintValidatorContext context) {

        if (value == null) {
            return false;
        }

        Enum<?>[] enumConstants = this.validEnum.enumClass().getEnumConstants();
        if (enumConstants == null) {
            return false;
        }

        return Arrays.stream(enumConstants).anyMatch(e->e.name().equals(value.toString() ));

    }
}