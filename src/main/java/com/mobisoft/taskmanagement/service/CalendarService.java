package com.mobisoft.taskmanagement.service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.taskmanagement.dto.ProjectDTO;
import com.mobisoft.taskmanagement.dto.TaskDTO;
import com.mobisoft.taskmanagement.dto.UserDTO;
import com.mobisoft.taskmanagement.entity.Project;
import com.mobisoft.taskmanagement.entity.Task;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.repository.TaskRepository;

@Service
public class CalendarService {

    @Autowired
    private TaskRepository taskRepository;

    // Méthode pour récupérer toutes les tâches
    public List<TaskDTO> findAllTasksCalendars() {
        
        try {
            List<Task> tasks = taskRepository.findAll();
            return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de toutes les tâches: " + e.getMessage(), e);
        }
    }


    private TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setTaskId(task.getTaskId());
        taskDTO.setTaskCode(task.getTaskCode());
        taskDTO.setTaskName(task.getTaskName());
        taskDTO.setTaskDescription(task.getTaskDescription());
        taskDTO.setTaskPriority(task.getTaskPriority().name());
        taskDTO.setTaskState(task.getTaskState().name());
        taskDTO.setPrioColor(task.getPrioColor());
        taskDTO.setStateColor(task.getStateColor());
        taskDTO.setTaskNombreHeurs(task.getTaskNombreHeurs());
        taskDTO.setTaskNombreJours(task.getTaskNombreJours());
        taskDTO.setProgression(task.getProgression());
        taskDTO.setColorCode(task.getColorCode());
        taskDTO.setIsValides(task.getIsValides());
    
        // Convertir les dates en chaînes de caractères
        taskDTO.setTaskCreatedAt(task.getTaskCreatedAt() != null ? task.getTaskCreatedAt().toString() : null);
        taskDTO.setTaskUpdatedAt(task.getTaskUpdatedAt() != null ? task.getTaskUpdatedAt().toString() : null);
        taskDTO.setTaskStartDate(task.getTaskStartDate() != null ? task.getTaskStartDate().toString() : null);
        taskDTO.setTaskEndDate(task.getTaskEndDate() != null ? task.getTaskEndDate().toString() : null);
        taskDTO.setAlerteDate(task.getAlerteDate() != null ? task.getAlerteDate().toString() : null);
    
        // Convertir les objets liés (Project, User) en DTO
        if (task.getProject() != null) {
            taskDTO.setProject(convertProjectToDTO(task.getProject()));
        }
        if (task.getUser() != null) {
            taskDTO.setUser(convertUserToDTO(task.getUser()));
        }
        if (task.getAssigned() != null) {
            taskDTO.setAssignedUser(convertUserToDTO(task.getAssigned()));
        }
    
        return taskDTO;
    }
    


    private TaskDTO convertToDTO2(Task task) {
    TaskDTO taskDTO = new TaskDTO();

    taskDTO.setTaskId(task.getTaskId());
    taskDTO.setTaskCode(task.getTaskCode());
    taskDTO.setTaskName(task.getTaskName());
    taskDTO.setTaskDescription(task.getTaskDescription());
    taskDTO.setTaskPriority(task.getTaskPriority().name());
    taskDTO.setTaskState(task.getTaskState().name());
    taskDTO.setPrioColor(task.getPrioColor());
    taskDTO.setStateColor(task.getStateColor());
    taskDTO.setTaskNombreHeurs(task.getTaskNombreHeurs());
    taskDTO.setTaskNombreJours(task.getTaskNombreJours());
    taskDTO.setProgression(task.getProgression());
    taskDTO.setColorCode(task.getColorCode());
    taskDTO.setIsValides(task.getIsValides());

    // Convertir les dates en chaînes de caractères
    OffsetDateTime taskCreatedAt = task.getTaskCreatedAt();
    String taskCreatedAtAsString = (taskCreatedAt != null) ? taskCreatedAt.toString() : null;
    taskDTO.setTaskCreatedAt(taskCreatedAtAsString);

    OffsetDateTime taskUpdatedAt = task.getTaskUpdatedAt();
    String taskUpdatedAtAsString = (taskUpdatedAt != null) ? taskUpdatedAt.toString() : null;
    taskDTO.setTaskUpdatedAt(taskUpdatedAtAsString);

    LocalDateTime taskStartDate = task.getTaskStartDate();
    String taskStartDateAsString = (taskStartDate != null) ? taskStartDate.toString() : null;
    taskDTO.setTaskStartDate(taskStartDateAsString);

    LocalDateTime taskEndDate = task.getTaskEndDate();
    String taskEndDateAsString = (taskEndDate != null) ? taskEndDate.toString() : null;
    taskDTO.setTaskEndDate(taskEndDateAsString);

    LocalDateTime alerteDate = task.getAlerteDate();
    String alerteDateAsString = (alerteDate != null) ? alerteDate.toString() : null;
    taskDTO.setAlerteDate(alerteDateAsString);

    // Convertir les objets liés (Project, User)
    taskDTO.setProjectId(task.getProject() != null ? task.getProject().getProjectId() : null);
    taskDTO.setUserId(task.getUser() != null ? task.getUser().getUserId() : null);
    taskDTO.setAssigned(task.getAssigned() != null ? task.getAssigned().getUserId() : null);

    return taskDTO;
}

    // Méthode pour convertir User en UserDTO
    private UserDTO convertUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setLastname(user.getLastname());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setPhone(user.getPhone());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setFonction(user.getFonction());
        userDTO.setToken(user.getToken());
        userDTO.setProfil(user.getProfil());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    // Méthode pour convertir Project en ProjectDTO
    private ProjectDTO convertProjectToDTO(Project project) {
        
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setProjectState(project.getProjectState().name());
        projectDTO.setProjectPriority(project.getProjectPriority().name());
        projectDTO.setProjectDescription(project.getProjectDescription());
        projectDTO.setProjectCodes(project.getProjectCodes());
        projectDTO.setProjectNombreJours(project.getProjectNombreJours());

        projectDTO.setPrioColor(project.getPrioColor());
        projectDTO.setStateColor(project.getStateColor());

        int progressInt = project.getProgress();
        projectDTO.setProgress(progressInt);

        projectDTO.setUserId(project.getUser().getUserId());

        LocalDateTime projectStartDate = project.getProjectStartDate();
        String projectStartDateAsString = projectStartDate.toString();
        projectDTO.setProjectStartDate(projectStartDateAsString);

        LocalDateTime projectEndDate = project.getProjectEndDate();
        String projectEndDateAsString = projectEndDate.toString();
        projectDTO.setProjectEndDate(projectEndDateAsString);

        OffsetDateTime projectCreaeDate = project.getProjectCreatedAt();
        String projectCreaeDateAsString = projectCreaeDate.toString();
        projectDTO.setProjectCreatedAt(projectCreaeDateAsString);

        OffsetDateTime projectUpdateDate = project.getProjectUpdatedAt();
        String projectUpdatedAtAsString = projectUpdateDate.toString();
        
        projectDTO.setProjectUpdatedAt(projectUpdatedAtAsString);

        return projectDTO;
    }



}
