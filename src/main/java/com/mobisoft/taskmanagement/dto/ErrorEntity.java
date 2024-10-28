package com.mobisoft.taskmanagement.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorEntity<T> {
    private int code;
    private String messages;
    private T data;
}
