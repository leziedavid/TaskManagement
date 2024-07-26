package com.mobisoft.taskmanagement.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.dto.ProjectDTO;
import com.mobisoft.taskmanagement.dto.ProjetDetailsDTO;
import com.mobisoft.taskmanagement.dto.ProjetUsersDTO;
import com.mobisoft.taskmanagement.dto.UserProjectDTO;
import com.mobisoft.taskmanagement.entity.Priority;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.service.ProjectService;
import com.mobisoft.taskmanagement.service.UserProjectService;

import jakarta.ws.rs.core.MediaType;


@RestController
@Validated
@RequestMapping("/api/v1")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private UserProjectService userProjectService;

    @PostMapping(value = "/projects", consumes = MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity<BaseResponse<ProjectDTO>> createProject(@ModelAttribute ProjectDTO projectDTO) {
        // System.out.println(projectDTO.getProjectDescription());
        //Enregistrer le projet avec les données reçues
        ProjectDTO createdProject = projectService.createProject(projectDTO);
        // Création de la réponse à retourner
        BaseResponse<ProjectDTO> response = new BaseResponse<>(201, "Projet créé avec succès", createdProject);
        // Retourner la réponse avec le statut HTTP 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/projects/files/addNewsFile", consumes = MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity<BaseResponse<ProjectDTO>> addNewsFile(@ModelAttribute ProjectDTO projectDTO) {
        //Enregistrer le projet avec les données reçues
        ProjectDTO addNewsFiles = projectService.addNewsFiles(projectDTO);
        // Création de la réponse à retourner
        BaseResponse<ProjectDTO> response = new BaseResponse<>(201, "Projet créé avec succès", addNewsFiles);
        // Retourner la réponse avec le statut HTTP 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }



    @GetMapping("/projects/{id}")
    public ResponseEntity<BaseResponse<ProjectDTO>> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        BaseResponse<ProjectDTO> response = new BaseResponse<>(200, "Détails du projet", project);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/projects/getProjectByCodes/{id}")
    public ResponseEntity<BaseResponse<ProjectDTO>> getProjectByCodes(@PathVariable String id) {
        ProjectDTO project = projectService.getProjectByCodes(id);
        BaseResponse<ProjectDTO> response = new BaseResponse<>(200, "Détails du projet", project);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/projects/getAllProjects")
    public ResponseEntity<BaseResponse<List<ProjectDTO>>> getAllProjects() {
        List<ProjectDTO> projects = projectService.findAllProjects();
        BaseResponse<List<ProjectDTO>> response = new BaseResponse<>(200, "Liste des projets", projects);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // @PutMapping(value="/projects/update/{id}",consumes = MediaType.MULTIPART_FORM_DATA)
    // public ResponseEntity<BaseResponse<ProjectDTO>> updateProject(@PathVariable String id, @Validated @RequestBody ProjectDTO projectDTO) {
    //     ProjectDTO updatedProject = projectService.updateProject(id, projectDTO);
    //     BaseResponse<ProjectDTO> response = new BaseResponse<>(200, "Projet mis à jour avec succès", updatedProject);
    //     return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    @PutMapping(value="/projects/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity<BaseResponse<ProjectDTO>> updateProject(@PathVariable String id, @ModelAttribute  ProjectDTO projectDTO) {
    ProjectDTO updatedProject = projectService.updateProject(id, projectDTO);
    BaseResponse<ProjectDTO> response = new BaseResponse<>(200, "Projet mis à jour avec succès", updatedProject);
    return new ResponseEntity<>(response, HttpStatus.OK);
}


    // Endpoint pour mettre à jour la priorité et la couleur du projet
    @PutMapping("/projects/updatePriority/{id}")
    public ResponseEntity<BaseResponse<ProjectDTO>> updatePriority(
            @PathVariable Long id,
            @RequestParam Priority priority,
            @RequestParam String Colors) {
        ProjectDTO updatedProject = projectService.updatePriorityAndColor(id, priority, Colors);
        BaseResponse<ProjectDTO> response = new BaseResponse<>(200, "La priorité et couleur mises à jour avec succès", updatedProject);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // Endpoint pour mettre à jour la priorité et la couleur du projet
    @PutMapping("/projects/updateState/{id}")
    public ResponseEntity<BaseResponse<ProjectDTO>> updateState(
            @PathVariable Long id,
            @RequestParam State state,
            @RequestParam String Colors) {
        ProjectDTO updatedProject = projectService.updateStateAndColor(id, state, Colors);
        BaseResponse<ProjectDTO> response = new BaseResponse<>(200, "le status et couleur mises à jour avec succès", updatedProject);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    

    @DeleteMapping("/projects/delete/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Projet supprimé avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/projects/detail/{projetId}")
        public ResponseEntity<BaseResponse<ProjetDetailsDTO>> getProjetDetails(@PathVariable String projetId) {
        ProjetDetailsDTO projetDetails = projectService.getProjetDetails(projetId);
        BaseResponse<ProjetDetailsDTO> response = new BaseResponse<>(200, "Projet mis à jour avec succès", projetDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
        // return ResponseEntity.ok(projetDetails);
    }

    @GetMapping("/projects/usersliste/{projetId}")
    public ResponseEntity<BaseResponse<List<User>>> getUsersByProjectId(@PathVariable String projetId) {
        List<User> users = projectService.getUsersByProjectId(projetId);
        BaseResponse<List<User>> response = new BaseResponse<>(HttpStatus.OK.value(), "Liste des utilisateurs du projet récupérée avec succès", users);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/projects/projetUsersliste/{projetId}")
    public ResponseEntity<BaseResponse<ProjetUsersDTO>> getProjetUsers(@PathVariable String projetId) {
        ProjetUsersDTO users = projectService.getProjetUsers(projetId);
        BaseResponse<ProjetUsersDTO> response = new BaseResponse<>(HttpStatus.OK.value(),
                "Liste des utilisateurs du projet récupérée avec succès", users);
        return ResponseEntity.ok(response);
    }


    @PostMapping("/projects/assignUsers/{projectId}/assignUsers")
    public ResponseEntity<BaseResponse<String>> assignUsersToProject(
            @PathVariable("projectId") String projectId,
            @RequestParam("users") String usersJson
    ) throws JsonMappingException, JsonProcessingException {

        // Convertir usersJson en objet Java si nécessaire
        UserProjectDTO userProjectDTO = mapper.readValue(usersJson, UserProjectDTO.class);
        projectService.assignUsersToProject(projectId, userProjectDTO);

        // Retourner une réponse de succès si l'opération a réussi
        BaseResponse<String> response = new BaseResponse<>(HttpStatus.OK.value(), "Utilisateur ajouté au projet avec succès", null);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/projects/removeUserFromProject/{projectId}/users/{userId}")
    public ResponseEntity<BaseResponse<String>> removeUserFromProject(

            @PathVariable("projectId") String projectId,
            @PathVariable("userId") Long userIdToRemove) {

        try {
            userProjectService.removeUserFromProject(projectId, userIdToRemove);
            // Si la suppression réussit, construire et retourner une réponse de succès
            BaseResponse<String> response = new BaseResponse<>(HttpStatus.OK.value(), "Utilisateur supprimé du projet avec succès", null);
            return ResponseEntity.ok(response);

        } catch (Exception e) {

            // En cas d'erreur, construire et retourner une réponse d'erreur
            BaseResponse<String> errorResponse = new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), null);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);

        }
    }

    // Endpoint pour mettre à jour le leader du projet
    @PutMapping("/projects/updateGroupLeader/{projectId}")
    public ResponseEntity<BaseResponse<String>> updateGroupLeader(
            @PathVariable String projectId,
            @RequestParam Long currentLeaderId,
            @RequestParam Long newLeaderId) {
        String message = userProjectService.updateProjectLeader(projectId, currentLeaderId, newLeaderId);
        BaseResponse<String> response = new BaseResponse<>(HttpStatus.OK.value(), message, null);
        return ResponseEntity.ok(response);
    }


}
