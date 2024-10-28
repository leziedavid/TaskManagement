package com.mobisoft.taskmanagement.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.dto.ProjectDTO;
import com.mobisoft.taskmanagement.dto.ProjectResponse;
import com.mobisoft.taskmanagement.dto.ProjectWithTasksDTO;
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
        public ResponseEntity<BaseResponse<ProjectResponse>> getAllProjects(
        @RequestHeader("Authorization") String authorizationHeader,
        @RequestParam(value = "page", defaultValue = "1") int page,
        @RequestParam(value = "size", defaultValue = "10") int size,
        @RequestParam(value = "sortBy", defaultValue = "projectId") String sortBy,
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

    try {
        // Appeler le service pour obtenir la réponse avec les projets et le nombre total d'éléments
        ProjectResponse projectResponse = projectService.findAllProjects(token, page, size, sortBy,userId);

        // Construire la réponse
        BaseResponse<ProjectResponse> response = new BaseResponse<>(200, "Liste des projets", projectResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);

    } catch (Exception e) {
        // Gérer les exceptions et les erreurs ici
        BaseResponse<ProjectResponse> response = new BaseResponse<>(500, "Erreur lors de la récupération des projets", null);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


    @GetMapping("/projects/statistics")
    public ResponseEntity<BaseResponse<Map<String, Long>>> getProjectStatistics(@RequestHeader("Authorization") String authorizationHeader) {
        // Extraire le token de l'en-tête Authorization
        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7) : authorizationHeader;

        try {
            // Appeler le service pour obtenir les statistiques avec le token
            Map<String, Long> statistics = projectService.getProjectStatistics(token);

            // Construire la réponse
            BaseResponse<Map<String, Long>> response = new BaseResponse<>(200, "Statistiques des projets", statistics);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            // Gérer les exceptions et les erreurs ici
            BaseResponse<Map<String, Long>> response = new BaseResponse<>(500, "Erreur lors de la récupération des statistiques", null);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

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
        BaseResponse<ProjetDetailsDTO> response = new BaseResponse<>(200, "detail des projet recuperer  avec succès", projetDetails);
        return new ResponseEntity<>(response, HttpStatus.OK);
        // return ResponseEntity.ok(projetDetails);
    }

    @GetMapping("/projects/usersliste/{projetId}")
    public ResponseEntity<BaseResponse<List<User>>> getUsersByProjectId(@PathVariable String projetId) {
        List<User> users = projectService.getUsersByProjectId(projetId);
        BaseResponse<List<User>> response = new BaseResponse<>(HttpStatus.OK.value(), "Liste des utilisateurs du projet récupérée avec succès", users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/projects/getProjectUsersById/{projetId}")
    public ResponseEntity<BaseResponse<List<User>>> getProjectUsersById(@PathVariable Long projetId) {
        List<User> users = projectService.getProjectUsersById(projetId);
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

    // @GetMapping("projects/filter2")
    // public BaseResponse<List<ProjectDTO>> getFilteredProjects2(

    //         @RequestParam(required = false) Priority priority,
    //         @RequestParam(required = false) State state,
    //         @RequestParam(required = false) Long departmentId,
    //         @RequestParam(required = false) String userIds,  // Passer comme chaîne JSON
    //         @RequestParam(required = false) Integer progress,
    //         @RequestParam(required = false) LocalDateTime startDate,
    //         @RequestParam(required = false) LocalDateTime endDate,
    //         @RequestHeader("Authorization") String authorizationHeader,
    //         @RequestParam(value = "page", defaultValue = "0") int page,
    //         @RequestParam(value = "size", defaultValue = "10") int size,
    //         @RequestParam(value = "sortBy", defaultValue = "projectCreatedAt") String sortBy) {

    //     Optional<Priority> optionalPriority = Optional.ofNullable(priority);
    //     Optional<State> optionalState = Optional.ofNullable(state);
    //     Optional<Long> optionalDepartmentId = Optional.ofNullable(departmentId);
    //     Optional<List<Long>> optionalUserIds = parseUserIds(userIds);  // Convertir la chaîne JSON en liste
    //     Optional<Integer> optionalProgress = Optional.ofNullable(progress);
    //     Optional<LocalDateTime> optionalStartDate = Optional.ofNullable(startDate);
    //     Optional<LocalDateTime> optionalEndDate = Optional.ofNullable(endDate);
    //     // Extraire le token de l'en-tête Authorization
    //     String token = authorizationHeader.startsWith("Bearer ")
    //     ? authorizationHeader.substring(7) : authorizationHeader;

    //     List<ProjectDTO> projects = projectService.findFilteredProjects2(
    //             optionalPriority,
    //             optionalState,
    //             optionalDepartmentId,
    //             optionalUserIds,
    //             optionalProgress,
    //             optionalStartDate,
    //             optionalEndDate,
    //             token,
    //             page,
    //             size);

    //     return new BaseResponse<>(200, "Liste des projets", projects);
    // }

    @GetMapping("projects/filter")
    public BaseResponse<ProjectResponse> getFilteredProjects(
            @RequestParam(required = false) Priority priority,
            @RequestParam(required = false) State state,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) String userIds, // Passer comme chaîne JSON
            @RequestParam(required = false) String progress, // Intervalle de pourcentage sous forme de chaîne "0-10"
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sortBy", defaultValue = "projectCreatedAt") String sortBy) {
    
            Optional<Priority> optionalPriority = Optional.ofNullable(priority);
            Optional<State> optionalState = Optional.ofNullable(state);
            Optional<Long> optionalDepartmentId = Optional.ofNullable(departmentId);
            Optional<List<Long>> optionalUserIds = parseUserIds(userIds);  // Convertir la chaîne JSON en liste
            Optional<int[]> optionalProgress = parseProgress(progress); // Convertir la chaîne d'intervalle en tableau
            Optional<LocalDateTime> optionalStartDate = Optional.ofNullable(startDate);
            Optional<LocalDateTime> optionalEndDate = Optional.ofNullable(endDate);
    
            // Extraire le token de l'en-tête Authorization
            String token = authorizationHeader.startsWith("Bearer ")
                    ? authorizationHeader.substring(7) : authorizationHeader;
    
        // Appeler le service pour obtenir la réponse paginée
        ProjectResponse projectResponse = projectService.findFilteredProjects(
                optionalPriority,
                optionalState,
                optionalDepartmentId,
                optionalUserIds,
                optionalProgress,
                optionalStartDate,
                optionalEndDate,
                token,
                page,
                size,
                sortBy); // Inclure sortBy
    
        return new BaseResponse<>(200, "Liste des projets", projectResponse);
    }
    

    private Optional<int[]> parseProgress(String progress) {
    if (progress == null || progress.isEmpty()) {
        return Optional.empty();
    }
    String[] parts = progress.split("-");
    if (parts.length != 2) {
        throw new IllegalArgumentException("Invalid format for progress: " + progress);
    }
    try {
        int start = Integer.parseInt(parts[0].trim());
        int end = Integer.parseInt(parts[1].trim());
        return Optional.of(new int[] {start, end});
    } catch (NumberFormatException e) {
        throw new IllegalArgumentException("Invalid format for progress: " + progress, e);
    }
}


    private Optional<List<Long>> parseUserIds(String userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Optional.empty();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<Long> ids = mapper.readValue(userIds, new TypeReference<List<Long>>() {});
            return Optional.of(ids);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid format for userIds: " + userIds, e);
        }
    }

    @GetMapping("projects/search")
    public BaseResponse<List<ProjectDTO>> searchProjects(
            @RequestParam(required = false) String projectName,  // Recherche par nom de projet
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestHeader("Authorization") String authorizationHeader) {
        // Extraire le token de l'en-tête Authorization
        String token = authorizationHeader.startsWith("Bearer ") ? authorizationHeader.substring(7) : authorizationHeader;

        List<ProjectDTO> projects = projectService.searchProjectsByName(projectName, token, page, size);

        return new BaseResponse<>(200, "Liste des projets", projects);
    }


    @GetMapping("projects/user/{userId}")
    public ResponseEntity<BaseResponse<List<ProjectWithTasksDTO>>> getProjectsByUser(@PathVariable Long userId) {
        List<ProjectWithTasksDTO> projects = projectService.getProjectsWithTasksByUser(userId); // Appelle le service avec l'ID de l'utilisateur
        BaseResponse<List<ProjectWithTasksDTO>> response = new BaseResponse<>(200, "Projets de l'utilisateur", projects);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
