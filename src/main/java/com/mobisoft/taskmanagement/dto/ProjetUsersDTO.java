package com.mobisoft.taskmanagement.dto;

import java.util.List;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mobisoft.taskmanagement.entity.UserProject;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ProjetUsersDTO {

    private List<UserProject> users;
    
    public  ProjetUsersDTO(List<UserProject> users) {
        this.users = users;
    }
}


