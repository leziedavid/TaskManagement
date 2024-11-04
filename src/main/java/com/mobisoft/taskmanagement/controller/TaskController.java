package com.mobisoft.taskmanagement.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.dto.TaskDTO;
import com.mobisoft.taskmanagement.dto.TaskDetailsDTO;
import com.mobisoft.taskmanagement.entity.Priority;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.service.CalendarService;
import com.mobisoft.taskmanagement.service.TaskService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;

@RestController
@Validated
@RequestMapping("/api/v1")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private CalendarService calendarService;

    @PostMapping("/tasks/addTasks")
    public ResponseEntity<BaseResponse<TaskDTO>> createTask(@Validated  @RequestBody TaskDTO taskDTO) {
        
        TaskDTO createdTask = taskService.createTask(taskDTO);
        BaseResponse<TaskDTO> response = new BaseResponse<>(201, "Tâche créée avec succès", createdTask);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/tasks/{taskCode}/difficulty")
    public ResponseEntity<TaskDTO> updateTaskDifficulty(@PathVariable String taskCode, @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateDifficulty(taskCode, taskDTO);
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/tasks/fetchTaskById/{id}")
    public ResponseEntity<BaseResponse<TaskDTO>> getTaskById(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        BaseResponse<TaskDTO> response = new BaseResponse<>(200, "Détails de la tâche", task);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @GetMapping("/tasks/getTasksByProjectId/{id}")
    public ResponseEntity<BaseResponse<List<TaskDTO>>> getTasksByProjectId(@PathVariable Long id) {
        List<TaskDTO> tasks = taskService.getTasksByProjectId(id);
        BaseResponse<List<TaskDTO>> response = new BaseResponse<>(200, "Détails de la tâche", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tasks/getAllTasksByProjectId/{id}")
    public ResponseEntity<BaseResponse<List<TaskDTO>>> getAllTasksByProjectId(
        @PathVariable String id,
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestHeader(value = "userId", required = false) String userIdStr) {

        // Extraire le token de l'en-tête Authorization
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;
        Long userId = null;
            if (userIdStr != null) {
                try {
                    userId = Long.valueOf(userIdStr); // Convertit le String en Long
                } catch (NumberFormatException e) {
                    // Gérer l'erreur de conversion si nécessaire
                    System.err.println("Invalid User-ID format: " + userIdStr);
                }
            }

        List<TaskDTO> tasks = taskService.getAllTasksByProjectId(id,token,userId);

        BaseResponse<List<TaskDTO>> response = new BaseResponse<>(200, "Détails de la tâche", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    

    @GetMapping("/tasks/getAllTasks")
    public ResponseEntity<BaseResponse<List<TaskDTO>>> getAllTasks() {
        List<TaskDTO> tasks = taskService.findAllTasks();
        BaseResponse<List<TaskDTO>> response = new BaseResponse<>(200, "Liste des tâches", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/tasks/calendars")
    public ResponseEntity<BaseResponse<List<TaskDTO>>> findAllTasksCalendars() {
        List<TaskDTO> tasks = calendarService.findAllTasksCalendars();
        BaseResponse<List<TaskDTO>> response = new BaseResponse<>(200, "Liste des tâches", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/tasks/update/{id}")
    public ResponseEntity<BaseResponse<TaskDTO>> updateTask(@PathVariable Long id,@Validated @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
        BaseResponse<TaskDTO> response = new BaseResponse<>(200, "Tâche mise à jour avec succès", updatedTask);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/tasks/delete/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Tâche supprimée avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint pour mettre à jour la priorité et la couleur du projet
    @PutMapping("/tasks/updateTaskState/{id}")
    public ResponseEntity<BaseResponse<TaskDTO>> updateTaskState(
            @PathVariable Long id,
            @RequestParam State state,
            @RequestParam String Colors,
            @RequestParam String projetsId,
            @RequestParam Long steps
            
            ) {
        TaskDTO updatedTask = taskService.updateTaskStateAndColors(id, state, Colors,projetsId,steps);
        BaseResponse<TaskDTO> response = new BaseResponse<>(200, "Le status et couleur mises à jour avec succès", updatedTask);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

        // Endpoint pour mettre à jour la priorité et la couleur du projet
        @PutMapping("/tasks/validteTaskState/{id}")
        public ResponseEntity<BaseResponse<TaskDTO>> validteTaskState(
                @PathVariable Long id,
                @RequestParam Integer state,
                @RequestParam String Colors,
                @RequestParam String projetsId
                ) {
            TaskDTO updatedTask = taskService.validteTaskState(id, state, Colors,projetsId);
            BaseResponse<TaskDTO> response = new BaseResponse<>(200, "Le status et couleur mises à jour avec succès", updatedTask);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    // detail des task :

    @GetMapping("/tasks/detail/{taskCode}")
    public ResponseEntity<BaseResponse<TaskDetailsDTO>> getTaskDetailsByCode(@PathVariable String taskCode) {
        // Appel au service pour obtenir les détails de la tâche
        TaskDetailsDTO taskDetails = taskService.getTaskDetailsByCode(taskCode);
        // Création de la réponse encapsulée
        BaseResponse<TaskDetailsDTO> response = new BaseResponse<>(200, "Détails de la tâche récupérés avec succès", taskDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // mettre a jour l'id de l'utilisateur  dans Task
    @PutMapping("/tasks/updateUsersTask/{taskId}")
    public ResponseEntity<BaseResponse<TaskDTO>> updateUsersTask(@PathVariable Long taskId,@RequestParam Long assignedUserId) {
        // Mettre à jour la tâche avec le nouvel utilisateur attribué
        TaskDTO updatedTask = taskService.updateAssignedUser(taskId, assignedUserId);
        // Créer la réponse
        BaseResponse<TaskDTO> response = new BaseResponse<>(200, "Task updated successfully", updatedTask);
        // Retourner la réponse
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/tasks/{projectId}/tasks/{taskId}")
    public ResponseEntity<BaseResponse<TaskDTO>> updateProjectAndTask(
            @PathVariable Long projectId,
            @PathVariable Long taskId,
            @RequestParam Integer newState,
            @RequestParam String selectedColors) {
        // Appel du service pour mettre à jour le projet et la tâche
        TaskDTO updatedTask = taskService.updateProjectAndTask(projectId, taskId, newState, selectedColors);
        // Créer la réponse
        BaseResponse<TaskDTO> response = new BaseResponse<>(200, "Le projet et la tâche ont été mis à jour avec succès", updatedTask);
        // Retourner la réponse avec le code HTTP OK
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    // Endpoint pour mettre à jour la date d'alerte d'une tâche
    @PutMapping("/tasks/updateAlerteDate/{id}")
    public ResponseEntity<BaseResponse<TaskDTO>> updateAlerteDate(
            @PathVariable Long id,
            @RequestParam @NotNull(message = "La nouvelle date d'alerte ne peut pas être nulle") String newAlerteDate) {

        try {
            TaskDTO updatedTask = taskService.updateTaskAlerteDate(id, newAlerteDate);
            BaseResponse<TaskDTO> response = new BaseResponse<>(200, "La date d'alerte a été mise à jour avec succès", updatedTask);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (EntityNotFoundException e) {
            BaseResponse<TaskDTO> response = new BaseResponse<>(404, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);

        } catch (IllegalArgumentException e) {

            BaseResponse<TaskDTO> response = new BaseResponse<>(400, e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }


        // Endpoint pour mettre à jour la priorité et la couleur du projet
    @PutMapping("/tasks/changeTaskPriority/{id}")
    public ResponseEntity<BaseResponse<TaskDTO>> changeTaskPriority(
            @PathVariable Long id,
            @RequestParam Priority priority,
            @RequestParam String Colors) {
            TaskDTO updatedProject = taskService.updatePriorityAndColor(id, priority, Colors);
        BaseResponse<TaskDTO> response = new BaseResponse<>(200, "La priorité et couleur mises à jour avec succès", updatedProject);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // changeTaskPriority


    @GetMapping("/tasks/filter")
    public BaseResponse<List<TaskDTO>> getFilteredTasks(
            @RequestParam(required = false) String projectCode, // Changer projectId de Long à String
            @RequestParam(required = false) Priority taskPriority,
            @RequestParam(required = false) State taskState,
            @RequestParam(required = false) Long assignedUserId,
            @RequestParam(required = false) Integer progression,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestHeader("Authorization") String authorizationHeader) {

        Optional<String> optionalProjectCode = Optional.ofNullable(projectCode);
        Optional<Priority> optionalTaskPriority = Optional.ofNullable(taskPriority);
        Optional<State> optionalTaskState = Optional.ofNullable(taskState);
        Optional<Long> optionalAssignedUserId = Optional.ofNullable(assignedUserId);
        Optional<Integer> optionalProgression = Optional.ofNullable(progression);
        Optional<LocalDateTime> optionalStartDate = Optional.ofNullable(startDate);
        Optional<LocalDateTime> optionalEndDate = Optional.ofNullable(endDate);
        String token = authorizationHeader.startsWith("Bearer ")
        ? authorizationHeader.substring(7) : authorizationHeader;

        List<TaskDTO> tasks = taskService.findFilteredTasks(
                optionalProjectCode,
                optionalTaskPriority,
                optionalTaskState,
                optionalAssignedUserId,
                optionalProgression,
                optionalStartDate,
                optionalEndDate,
                token);

        return new BaseResponse<>(200, "Liste des tâches", tasks);
    }

    

}