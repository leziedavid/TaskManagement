package com.mobisoft.taskmanagement.dto;

import java.util.List;

public class UsersResponse {
    
    private List<UserDepartmentDTO> users;
    private long totalElements;
    private long totalPages; // Ajoute ce champ si tu veux inclure le total des pages


    public UsersResponse(List<UserDepartmentDTO> users, long totalElements, long totalPages) {
        this.users = users;
        this.totalElements = totalElements;
        this.totalPages = totalPages;

    }

    public List<UserDepartmentDTO> getUsers() {
        return users;
    }

    public long getTotalElements() {
        return totalElements;
    }
    public long getTotalPages() {
        return totalPages;
    }
}
