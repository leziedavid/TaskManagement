package com.mobisoft.taskmanagement.controller;

import com.mobisoft.taskmanagement.dto.TaskDTO;
import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<BaseResponse<TaskDTO>> createTask(@Validated @RequestBody TaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        BaseResponse<TaskDTO> response = new BaseResponse<>(201, "Tâche créée avec succès", createdTask);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<TaskDTO>> getTaskById(@PathVariable Long id) {
        TaskDTO task = taskService.getTaskById(id);
        BaseResponse<TaskDTO> response = new BaseResponse<>(200, "Détails de la tâche", task);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getTasksByProjectId/{id}")
    public ResponseEntity<BaseResponse<List<TaskDTO>>> getTasksByProjectId(@PathVariable Long id) {
        List<TaskDTO> tasks = taskService.getTasksByProjectId(id);
        BaseResponse<List<TaskDTO>> response = new BaseResponse<>(200, "Détails de la tâche", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    

    @GetMapping("/getAllTasks")
    public ResponseEntity<BaseResponse<List<TaskDTO>>> getAllTasks() {
        List<TaskDTO> tasks = taskService.findAllTasks();
        BaseResponse<List<TaskDTO>> response = new BaseResponse<>(200, "Liste des tâches", tasks);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<TaskDTO>> updateTask(@PathVariable Long id,@Validated @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
        BaseResponse<TaskDTO> response = new BaseResponse<>(200, "Tâche mise à jour avec succès", updatedTask);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Tâche supprimée avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
