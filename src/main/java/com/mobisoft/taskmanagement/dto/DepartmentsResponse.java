package com.mobisoft.taskmanagement.dto;

import java.util.List;

public class DepartmentsResponse {

    private List<DepartmentDTO> departments;
    private long totalElements;
    private long totalPages; // Ajoute ce champ si tu veux inclure le total des pages

    public DepartmentsResponse(List<DepartmentDTO> departments, long totalElements, long totalPages) {
        this.departments = departments;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<DepartmentDTO> getDepartments() {
        return departments;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public long getTotalPages() {
        return totalPages;
    }
}
