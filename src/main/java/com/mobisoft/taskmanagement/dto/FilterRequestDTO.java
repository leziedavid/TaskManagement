package com.mobisoft.taskmanagement.dto;

import java.time.LocalDate;
import java.util.List;

import com.mobisoft.taskmanagement.entity.Priority;
import com.mobisoft.taskmanagement.entity.State;

public class FilterRequestDTO {


    private Priority priority;
    private State state;
    private Long departmentId;
    private List<Long> userIds; // Liste d'IDs d'utilisateur
    private Integer progress;
    private LocalDate startDate;
    private LocalDate endDate;

    // Getters et setters
    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }
    public State getState() { return state; }
    public void setState(State state) { this.state = state; }
    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
    public List<Long> getUserIds() { return userIds; }
    public void setUserIds(List<Long> userIds) { this.userIds = userIds; }
    public Integer getProgress() { return progress; }
    public void setProgress(Integer progress) { this.progress = progress; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

}
