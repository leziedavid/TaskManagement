package com.mobisoft.taskmanagement.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.dto.ProjectDTO;
import com.mobisoft.taskmanagement.dto.TaskDTO;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.service.TaskService;

@RestController
@Validated
@RequestMapping("/api/v1")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/tasks/addTasks")
    public ResponseEntity<BaseResponse<TaskDTO>> createTask(@Validated  @RequestBody TaskDTO taskDTO) {
        
        TaskDTO createdTask = taskService.createTask(taskDTO);
        BaseResponse<TaskDTO> response = new BaseResponse<>(201, "Tâche créée avec succès", createdTask);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/tasks/{id}")
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
    

    @GetMapping("/tasks/getAllTasks")
    public ResponseEntity<BaseResponse<List<TaskDTO>>> getAllTasks() {
        List<TaskDTO> tasks = taskService.findAllTasks();
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
            @RequestParam String Colors) {
        TaskDTO updatedTask = taskService.updateTaskStateAndColors(id, state, Colors);
        BaseResponse<TaskDTO> response = new BaseResponse<>(200, "Le status et couleur mises à jour avec succès", updatedTask);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // updateTaskState
}
