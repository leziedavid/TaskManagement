package com.mobisoft.taskmanagement.dto;

import java.util.List;

public class UserProjectDTO {
    
    private List<Long> usersId;
    private Long leaderId;

    public List<Long> getUsersId() {
        return this.usersId;
    }

    public void setUsersId(List<Long> usersId) {
        this.usersId = usersId;
    }

    public Long getLeaderId() {
        return this.leaderId;
    }

    public void setLeaderId(Long leaderId) {
        this.leaderId = leaderId;
    }

}
