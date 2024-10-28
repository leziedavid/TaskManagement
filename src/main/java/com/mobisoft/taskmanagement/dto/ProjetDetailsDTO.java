package com.mobisoft.taskmanagement.dto;

import java.util.List;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mobisoft.taskmanagement.entity.Project;
import com.mobisoft.taskmanagement.entity.UserProject;
import com.mobisoft.taskmanagement.entity.Task;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ProjetDetailsDTO {

    private Project projects;
    private List<UserProject> users;
    private List<Task> tasks;

    public ProjetDetailsDTO(Project projects, List<UserProject> users, List<Task> tasks) {
        this.projects = projects;
        this.users = users;
        this.tasks = tasks;
    }


    // getters et setters
}


