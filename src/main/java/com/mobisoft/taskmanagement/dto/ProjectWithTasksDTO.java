package com.mobisoft.taskmanagement.dto;

import lombok.Data;
import java.util.List;
import com.mobisoft.taskmanagement.entity.Task;

@Data

public class ProjectWithTasksDTO {
    private Long projectId;
    private String projectName;
    private String projectCodes;
    private List<Task> tasks;
}