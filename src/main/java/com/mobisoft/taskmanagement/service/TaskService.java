package com.mobisoft.taskmanagement.service;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.taskmanagement.dto.ProjectDTO;
import com.mobisoft.taskmanagement.dto.TaskDTO;
import com.mobisoft.taskmanagement.entity.Priority;
import com.mobisoft.taskmanagement.entity.Project;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.entity.Task;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.repository.TaskRepository;
import com.mobisoft.taskmanagement.repository.ProjectRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public TaskDTO createTask(TaskDTO taskDTO) {
        try {
            Task task = convertToEntity(taskDTO);
            Task savedTask = taskRepository.save(task);
            return convertToDTO(savedTask);
        } catch (Exception e) {
            throw new EntityNotFoundException("Erreur lors de la création de la tâche: " + e.getMessage());
        }
    }

    public TaskDTO getTaskById(Long taskId) {
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        return optionalTask.map(this::convertToDTO).orElseThrow(() -> new EntityNotFoundException("Aucune tâche trouvée avec l'ID: " + taskId));
    }

    public List<TaskDTO> getTasksByProjectId(Long projectId) {

        List<Task> tasks = taskRepository.findTasksByProjectId(projectId);

        if (tasks.isEmpty()) {

            throw new EntityNotFoundException("Aucune tâche trouvée pour le projet avec l'ID: " + projectId);
        }
        return tasks.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    public List<TaskDTO> findAllTasks() {
        try {
            List<Task> tasks = taskRepository.findAll();
            return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de toutes les tâches: " + e.getMessage());
        }
    }

    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("La tâche avec l'ID spécifié n'existe pas"));
        updateTaskFromDTO(task, taskDTO);
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    public boolean deleteTask(Long taskId) {
        if (!taskRepository.existsById(taskId)) {
            throw new EntityNotFoundException("La tâche avec l'ID spécifié n'existe pas");
        }
        taskRepository.deleteById(taskId);
        return true;
    }


        // Méthode pour mettre à jour du status et la couleur d'un projet
    public TaskDTO updateTaskStateAndColors(Long taskId, State newState, String selectedColors) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Le projet avec l'ID spécifié n'existe pas"));
        // Mise à jour de la priorité et de la couleur
        task.setTaskState(newState);
        task.setStateColor(selectedColors);
        task.setTaskUpdatedAt(OffsetDateTime.now());// Mettre à jour la date de mise à jour
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }


    private TaskDTO convertToDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
    
        taskDTO.setTaskId(task.getTaskId());
        taskDTO.setTaskName(task.getTaskName());
        taskDTO.setTaskDescription(task.getTaskDescription());
        taskDTO.setTaskPriority(task.getTaskPriority().name());
        taskDTO.setTaskState(task.getTaskState().name());
        taskDTO.setProjectId(task.getProject().getProjectId());
        taskDTO.setUserId(task.getUser().getUserId());
        taskDTO.setAssigned(task.getAssigned().getUserId()); // Champ assigned
        taskDTO.setProgression(task.getProgression()); // Nouveau champ
        taskDTO.setColorCode(task.getColorCode());
        taskDTO.setPrioColor(task.getPrioColor());
        taskDTO.setTaskNombreHeurs(task.getTaskNombreHeurs());
        taskDTO.setTaskNombreJours(task.getTaskNombreJours());
    
        LocalDate taskStartDate = task.getTaskStartDate();
        String taskStartDateAsString = taskStartDate.toString();
        taskDTO.setTaskStartDate(taskStartDateAsString);
    
        LocalDate taskEndDate = task.getTaskEndDate();
        String taskEndDateAsString = taskEndDate.toString();
        taskDTO.setTaskEndDate(taskEndDateAsString);
    
        return taskDTO;
    }
    

    private Task convertToEntity(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTaskName(taskDTO.getTaskName());
        task.setTaskDescription(taskDTO.getTaskDescription());

        task.setColorCode(taskDTO.getColorCode());
        task.setPrioColor(taskDTO.getPrioColor());
        task.setProgression(taskDTO.getProgression());
        task.setTaskNombreHeurs(taskDTO.getTaskNombreHeurs());
        task.setTaskNombreJours(taskDTO.getTaskNombreJours());

        task.setTaskPriority(Priority.valueOf(taskDTO.getTaskPriority()));
        task.setTaskState(State.valueOf(taskDTO.getTaskState()));
        
        // Ici nous allons recuperer l'id du projet grace a son code
        // String projectIdString = String.valueOf(taskDTO.getProjectId());
        // Project project = projectRepository.findByProjectCodes(projectIdString);
        
        Project project = projectRepository.findByProjectCodes(taskDTO.getProjectCodes());
        if (project == null) {
            throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + taskDTO.getProjectId());
        }
        Long projectId = project.getProjectId();
        // Project project = new Project();
        project.setProjectId(projectId);
        task.setProject(project);

        User user = new User();
        user.setUserId(taskDTO.getUserId());
        task.setUser(user);

        User UserAssigned = new User();
        UserAssigned.setUserId(taskDTO.getAssigned());
        task.setAssigned(UserAssigned);

        LocalDate taskStartDate = LocalDate.parse(taskDTO.getTaskStartDate());
        task.setTaskStartDate(taskStartDate);

        LocalDate taskEndDate = LocalDate.parse(taskDTO.getTaskEndDate());
        task.setTaskEndDate(taskEndDate);
        
        return task;
    }

    private void updateTaskFromDTO(Task task, TaskDTO taskDTO) {
        task.setTaskName(taskDTO.getTaskName());
        task.setTaskDescription(taskDTO.getTaskDescription());
        task.setColorCode(taskDTO.getColorCode());
        task.setTaskPriority(Priority.valueOf(taskDTO.getTaskPriority()));
        task.setTaskState(State.valueOf(taskDTO.getTaskState()));
        Project project = new Project();
        project.setProjectId(taskDTO.getProjectId());
        task.setProject(project);
        User user = new User();
        user.setUserId(taskDTO.getUserId());
        task.setUser(user);

        LocalDate taskStartDate = LocalDate.parse(taskDTO.getTaskStartDate());
        task.setTaskStartDate(taskStartDate);

        LocalDate taskEndDate = LocalDate.parse(taskDTO.getTaskEndDate());
        task.setTaskEndDate(taskEndDate);
    }



}
