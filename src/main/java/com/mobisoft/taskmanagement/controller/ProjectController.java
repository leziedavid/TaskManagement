package com.mobisoft.taskmanagement.controller;

import com.mobisoft.taskmanagement.dto.ProjectDTO;
import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<BaseResponse<ProjectDTO>> createProject(@Validated @RequestBody ProjectDTO projectDTO) {
        ProjectDTO createdProject = projectService.createProject(projectDTO);
        BaseResponse<ProjectDTO> response = new BaseResponse<>(201, "Projet créé avec succès", createdProject);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ProjectDTO>> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        BaseResponse<ProjectDTO> response = new BaseResponse<>(200, "Détails du projet", project);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllProjects")
    public ResponseEntity<BaseResponse<List<ProjectDTO>>> getAllProjects() {
        List<ProjectDTO> projects = projectService.findAllProjects();
        BaseResponse<List<ProjectDTO>> response = new BaseResponse<>(200, "Liste des projets", projects);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ProjectDTO>> updateProject(@PathVariable Long id,@Validated @RequestBody ProjectDTO projectDTO) {
        ProjectDTO updatedProject = projectService.updateProject(id, projectDTO);
        BaseResponse<ProjectDTO> response = new BaseResponse<>(200, "Projet mis à jour avec succès", updatedProject);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Projet supprimé avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
