package com.mobisoft.taskmanagement.dto;

import java.util.List;

public class ProjectResponse {
    
    private List<ProjectDTO> projects;
    private long totalElements;
    private long totalPages; // Ajoute ce champ si tu veux inclure le total des pages


    public ProjectResponse(List<ProjectDTO> projects, long totalElements, long totalPages) {
        this.projects = projects;
        this.totalElements = totalElements;
        this.totalPages = totalPages;

    }

    public List<ProjectDTO> getProjects() {
        return projects;
    }

    public long getTotalElements() {
        return totalElements;
    }
    public long getTotalPages() {
        return totalPages;
    }
}
