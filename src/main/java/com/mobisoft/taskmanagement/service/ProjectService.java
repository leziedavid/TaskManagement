package com.mobisoft.taskmanagement.service;


import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobisoft.taskmanagement.dto.ProjectDTO;
import com.mobisoft.taskmanagement.dto.ProjectResponse;
import com.mobisoft.taskmanagement.dto.ProjectWithTasksDTO;
import com.mobisoft.taskmanagement.dto.ProjetDetailsDTO;
import com.mobisoft.taskmanagement.dto.ProjetUsersDTO;
import com.mobisoft.taskmanagement.dto.UserProjectDTO;
import com.mobisoft.taskmanagement.dto.UserRoleDTO;
import com.mobisoft.taskmanagement.entity.Priority;
import com.mobisoft.taskmanagement.entity.Project;
import com.mobisoft.taskmanagement.entity.Role;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.entity.Task;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.entity.UserProject;
import com.mobisoft.taskmanagement.pagination.Page;
import com.mobisoft.taskmanagement.pagination.Paginator;
import com.mobisoft.taskmanagement.repository.ProjectRepository;
import com.mobisoft.taskmanagement.repository.TaskRepository;
import com.mobisoft.taskmanagement.repository.UserProjectRepository;
import com.mobisoft.taskmanagement.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;


@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserProjectService userProjectService;

    @Autowired
    private UserProjectRepository userProjectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private FileStorageService fileStorageService;

    @Autowired
    private UserService userService; // Injection de UserService

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        
    try {
        // Convertir le DTO en entité et sauvegarder dans la base de données
        Project project = convertToEntity(projectDTO);
        Project savedProject = projectRepository.save(project);

        // Créer une relation utilisateur-projet avec UserProjectService si des utilisateurs sont spécifiés
        if (projectDTO.getUsers() != null) {
            UserProjectDTO userProjectDTO = mapper.readValue(projectDTO.getUsers(), UserProjectDTO.class);
            userProjectService.createUserProject(savedProject, userProjectDTO);
        }

        // Récupérer le nombre de fichiers à traiter (peut être 0)
        int nbFiles = projectDTO.getNbfiles() != null ? Integer.parseInt(projectDTO.getNbfiles()) : 0;

        // Traiter chaque fichier si présent
        for (int i = 1; i <= nbFiles; i++) {
            String fichiersFieldName = "fichiers" + i;
            String titleFieldName = "title" + i;

            try {
                // Utilisation de la réflexion pour accéder aux champs MultipartFile et title
                Field fichiersField = ProjectDTO.class.getDeclaredField(fichiersFieldName);
                fichiersField.setAccessible(true);
                MultipartFile file = (MultipartFile) fichiersField.get(projectDTO);

                Field titleField = ProjectDTO.class.getDeclaredField(titleFieldName);
                titleField.setAccessible(true);
                String title = (String) titleField.get(projectDTO);

                if (file != null && !file.isEmpty()) {
                    // Gestion de l'upload du fichier et enregistrement dans la base de données
                    String publicId = fileStorageService.uploadFileWithTitle(file, title);
                    Long publicIdLong = Long.valueOf(publicId);
                    fileStorageService.assignFilesToProject(savedProject.getProjectId(), publicIdLong);
                }
                
            } catch (NoSuchFieldException | IllegalAccessException | NumberFormatException e) {
                // Capturer et relancer une exception adaptée pour les erreurs de réflexion ou de conversion
                throw new IllegalArgumentException("Erreur lors de la gestion des fichiers: " + e.getMessage(), e);
            }
        }

        // Convertir l'entité sauvegardée en DTO et le retourner
        return convertToDTO(savedProject);

    } catch (DataAccessException e) {
        // Capturer et relancer une exception spécifique pour les erreurs de base de données
        throw new PersistenceException("Erreur lors de la création du projet en base de données: " + e.getMessage(), e);

    } catch (JsonProcessingException e) {
        // Capturer et relancer une exception spécifique pour les erreurs de désérialisation JSON
        throw new IllegalArgumentException("Erreur lors de la désérialisation des utilisateurs: " + e.getMessage(), e);

    } catch (Exception e) {
        // Capturer et relancer une exception générale pour toutes les autres erreurs inattendues
        throw new RuntimeException("Erreur inattendue lors de la création du projet: " + e.getMessage(), e);
    }
}

    public ProjectDTO createProjectsx(ProjectDTO projectDTO) {

        try {
            
            Project project = convertToEntity(projectDTO);
            Project savedProject = projectRepository.save(project);

             // Créer une relation utilisateur-projet avec UserProjectService
                // Nous allons mapper nos données
            if (projectDTO.getUsers() != null){
                UserProjectDTO userProjectDTO = mapper.readValue(projectDTO.getUsers() , UserProjectDTO.class);
                // System.out.println(userProjectDTO.getUsersId());
                userProjectService.createUserProject(savedProject,userProjectDTO);
            }

            int nbFiles = Integer.parseInt(projectDTO.getNbfiles());

            for (int i = 1; i <= nbFiles; i++) {
                String fichiersFieldName = "fichiers" + i;
                String titleFieldName = "title" + i;

                // Utilisation de reflection pour accéder aux champs MultipartFile et title
                Field fichiersField = ProjectDTO.class.getDeclaredField(fichiersFieldName);
                fichiersField.setAccessible(true);
                MultipartFile file = (MultipartFile) fichiersField.get(projectDTO);

                Field titleField = ProjectDTO.class.getDeclaredField(titleFieldName);
                titleField.setAccessible(true);
                String title = (String) titleField.get(projectDTO);

                if (file != null && !file.isEmpty()) {
                    // Gestion de l'upload du fichier et enregistrement dans la base de données
                    String publicId =  fileStorageService.uploadFileWithTitle(file,title);
                    Long publicIdLong = Long.valueOf(publicId);
                    fileStorageService.assignFilesToProject(savedProject.getProjectId(), publicIdLong);
                }

            }
            
            return convertToDTO(savedProject);
            
        } catch (Exception e) {

            throw new EntityNotFoundException("Erreur lors de la création du projet: " + e.getMessage());
        }
    }

    public ProjectDTO addNewsFiles(ProjectDTO projectDTO) {

        System.out.println(projectDTO);
        
        try {
            
            // Project project = convertToEntity(projectDTO);

            Project projectbyCodes = projectRepository.findByProjectCodes(projectDTO.getProjectCodes());

            if (projectbyCodes == null) {
                throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + projectDTO.getProjectCodes());
            }
            Long projectId = projectbyCodes.getProjectId();

            int nbFiles = Integer.parseInt(projectDTO.getNbfiles());

            for (int i = 1; i <= nbFiles; i++) {
                String fichiersFieldName = "fichiers" + i;
                String titleFieldName = "title" + i;

                // Utilisation de reflection pour accéder aux champs MultipartFile et title
                Field fichiersField = ProjectDTO.class.getDeclaredField(fichiersFieldName);
                fichiersField.setAccessible(true);
                MultipartFile file = (MultipartFile) fichiersField.get(projectDTO);

                Field titleField = ProjectDTO.class.getDeclaredField(titleFieldName);
                titleField.setAccessible(true);
                String title = (String) titleField.get(projectDTO);

                if (file != null && !file.isEmpty()) {
                    // Gestion de l'upload du fichier et enregistrement dans la base de données
                    String publicId =  fileStorageService.uploadFileWithTitle(file,title);
                    Long publicIdLong = Long.valueOf(publicId);
                    fileStorageService.assignFilesToProject(projectId, publicIdLong);
                }

            }
            
            return convertToDTO(projectbyCodes);
            
        } catch (Exception e) {

            throw new EntityNotFoundException("Erreur lors de la création du projet: " + e.getMessage());
        }
    }
    
    public ProjectDTO getProjectById(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        return optionalProject.map(this::convertToDTO).orElseThrow(() -> new EntityNotFoundException("Aucun projet trouvé avec l'ID: " + projectId));
    }

    public ProjectDTO getProjectByCodes(String projectId) {
        
        Project projectbyCodes = projectRepository.findByProjectCodes(projectId);

        if (projectbyCodes == null) {
            throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + projectId);
        }
        return convertToDTO(projectbyCodes);
    }

    public ProjectResponse findAllProjects(String token, int page, int size, String sortBy, Long Iduser) {
        try {

            // UserRoleDTO userRoleDTO = userService.getUserRoleAndIdFromToken(token);

            UserRoleDTO userRoleDTO;
            // Obtenez les informations utilisateur à partir du token ou de userId
            if (Iduser != null) {
                userRoleDTO = userService.getUserRoleAndIdFromUserId(Iduser);
            } else {
                userRoleDTO = userService.getUserRoleAndIdFromToken(token);
            }

            Long userId = userRoleDTO.getUserId();
            Role role = userRoleDTO.getRole();
    
            List<Project> projects;
    
            if (role == Role.USER) {
                projects = projectRepository.findProjectsByUserId(userId);
            } else {
                projects = projectRepository.findAll();
            }
    
            // Convertir les entités en DTOs
            List<ProjectDTO> projectDTOs = projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    
            // Utiliser le paginator pour obtenir les éléments paginés
            Page<ProjectDTO> pagedProjects = Paginator.paginate(projectDTOs, page, size, sortBy);
    
            // Créer le ProjectResponse avec les éléments paginés
            return new ProjectResponse(pagedProjects.getContent(), pagedProjects.getTotalElements(), pagedProjects.getTotalPages());
    
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de tous les projets: " + e.getMessage());
        }
    }
    

    public List<ProjectDTO> findAllProjects1(String token) {
        try {
            UserRoleDTO userRoleDTO = userService.getUserRoleAndIdFromToken(token);


            Long userId = userRoleDTO.getUserId();
            Role role = userRoleDTO.getRole();

            System.out.println(userId);

            List<Project> projects;

            if (role == Role.USER) {
                // Si le rôle est "User", obtenir les projets assignés à l'utilisateur
                projects = projectRepository.findProjectsByUserId(userId);
            } else {
                // Sinon, obtenir tous les projets
                projects = projectRepository.findAll();
            }

            // Convertir les entités en DTOs
            return projects.stream().map(this::convertToDTO).collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de tous les projets: " + e.getMessage());
        }
    }


    public ProjectDTO updateProject(String projectId, ProjectDTO projectDTO) {

        Project project = projectRepository.findByProjectCodes(projectId);

        if (project == null) {
            throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + projectId);
        }

        Project projectData = updateProjectFromDTO(project, projectDTO);
        Project updatedProject = projectRepository.save(projectData);
        return convertToDTO(updatedProject);

    }

    // Méthode pour mettre à jour la priorité d'un projet
    public ProjectDTO updatePriority(Long projectId, Priority newPriority) {
        // System.out.println(newPriority);
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("Le projet avec l'ID spécifié n'existe pas"));

        // Mise à jour de la priorité
        project.setProjectPriority(newPriority);
        project.setProjectUpdatedAt(OffsetDateTime.now()); // Mettre à jour la date de mise à jour
        Project updatedProject = projectRepository.save(project);
        return convertToDTO(updatedProject);
    }

    // Méthode pour mettre à jour la priorité et la couleur d'un projet
    public ProjectDTO updatePriorityAndColor(Long projectId, Priority newPriority, String selectedColors) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("Le projet avec l'ID spécifié n'existe pas"));
        // Mise à jour de la priorité et de la couleur
        project.setProjectPriority(newPriority);
        project.setPrioColor(selectedColors);
        project.setProjectUpdatedAt(OffsetDateTime.now()); // Mettre à jour la date de mise à jour
        Project updatedProject = projectRepository.save(project);
        return convertToDTO(updatedProject);
    }
    // Méthode pour mettre à jour du status et la couleur d'un projet
    public ProjectDTO updateStateAndColor(Long projectId, State newState, String selectedColors) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("Le projet avec l'ID spécifié n'existe pas"));
        // Mise à jour de la priorité et de la couleur
        project.setProjectState (newState);
        project.setStateColor(selectedColors);
        project.setProjectUpdatedAt(OffsetDateTime.now()); // Mettre à jour la date de mise à jour
        Project updatedProject = projectRepository.save(project);
        return convertToDTO(updatedProject);
    }

    public boolean deleteProject(Long projectId) {

        // Vérifier si le projet existe
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException("Le projet avec l'ID spécifié n'existe pas");
        }

        // Supprimer les tâches associées au projet, s'il y en a
        taskRepository.findByProjectProjectId(projectId).forEach(taskRepository::delete);

        // Supprimer les entités UserProject associées au projet, s'il y en a
        userProjectRepository.findByProjectId(projectId).forEach(userProjectRepository::delete);

        // Supprimer le projet lui-même
        projectRepository.deleteById(projectId);

        return true;
    }

    private Project convertToEntity(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setProjectName(projectDTO.getProjectName());
        project.setProjectState(State.valueOf(projectDTO.getProjectState()));
        project.setProjectPriority(Priority.valueOf(projectDTO.getProjectPriority()));
        project.setProjectDescription(projectDTO.getProjectDescription());

        project.setProjectNombreJours(projectDTO.getProjectNombreJours());
        project.setPrioColor(projectDTO.getPrioColor());
        project.setStateColor(projectDTO.getStateColor());
        project.setProgress(projectDTO.getProgress());

        LocalDateTime projectStartDate = LocalDateTime.parse(projectDTO.getProjectStartDate());
        project.setProjectStartDate(projectStartDate);
        LocalDateTime projectEndDate = LocalDateTime.parse(projectDTO.getProjectEndDate());
        
        project.setProjectEndDate(projectEndDate);
        // Utilisation de CodeGenerator pour générer le code produit unique
        project.setProjectCodes(CodeGenerator.generateUniqueProjectCode());
        User user = new User();
        user.setUserId(projectDTO.getUserId());
        project.setUser(user);
        return project;
    }

    private ProjectDTO convertToDTO(Project project) {
        
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

    private Project updateProjectFromDTO(Project project, ProjectDTO projectDTO) {

        // Les lignes existantes restent inchangées
        project.setProjectName(projectDTO.getProjectName());
        project.setProjectState(State.valueOf(projectDTO.getProjectState()));
        project.setProjectPriority(Priority.valueOf(projectDTO.getProjectPriority()));
        project.setProjectDescription(projectDTO.getProjectDescription());

        LocalDateTime projectStartDate = LocalDateTime.parse(projectDTO.getProjectStartDate());
        project.setProjectStartDate(projectStartDate);
        LocalDateTime projectEndDate = LocalDateTime.parse(projectDTO.getProjectEndDate());
        project.setProjectEndDate(projectEndDate);
    
        User user = new User();
        user.setUserId(projectDTO.getUserId());
        project.setUser(user);

        project.setProjectNombreJours(projectDTO.getProjectNombreJours());
        project.setPrioColor(projectDTO.getPrioColor());
        project.setStateColor(projectDTO.getStateColor());
        project.setProgress(projectDTO.getProgress());
        project.setProjectUpdatedAt(OffsetDateTime.now()); // Mettre à jour la date de mise à jour
        return project;
    }
    
    public ProjetDetailsDTO getProjetDetails(String projectCodes) {
        
        Project project = projectRepository.findByProjectCodes(projectCodes);
        if (project == null) {
            throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + projectCodes);
        }
        
        Long projectId = project.getProjectId();
        List<UserProject> users = userProjectRepository.findByProjectId(projectId);
        List<Task> tasks = taskRepository.findByProjectProjectId(projectId);
        return new ProjetDetailsDTO(project, users, tasks);
    }

    public ProjetUsersDTO getProjetUsers(String projectCodes) {
        
        Project project = projectRepository.findByProjectCodes(projectCodes);
        if (project == null) {
            throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + projectCodes);
        }
        Long projectId = project.getProjectId();
        List<UserProject> users = userProjectRepository.findByProjectId(projectId);
        return new ProjetUsersDTO(users);
    }

    public List<User> getUsersByProjectId(String projectCodes) {

        Project project = projectRepository.findByProjectCodes(projectCodes);
        if (project == null) {
            throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + projectCodes);
        }

        Long projectId = project.getProjectId();
        return userProjectRepository.findUsersByProjectId(projectId);
    }

    public void assignUsersToProject(String projectId, UserProjectDTO userProjectDTO) {

        Project project = projectRepository.findByProjectCodes(projectId);

        if (project == null) {
            throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + projectId);
        }
        userProjectService.assignUsersToProject(project, userProjectDTO);

    }

    // AVEC PARGINATION

    public ProjectResponse findFilteredProjects(
            Optional<Priority> projectPriority,
            Optional<State> projectState,
            Optional<Long> departmentId,
            Optional<List<Long>> userIds,
            Optional<int[]> progressRange,
            Optional<LocalDateTime> projectStartDate,
            Optional<LocalDateTime> projectEndDate,
            String token,
            int page,
            int size,
            String sortBy) {

        UserRoleDTO userRoleDTO = userService.getUserRoleAndIdFromToken(token);
        Long userId = userRoleDTO.getUserId();
        Role role = userRoleDTO.getRole();

        List<Project> projects;

        if (role == Role.USER) {
            projects = projectRepository.findProjectsByUserId(userId);
        } else {
            projects = projectRepository.findAll();
        }

        // Appliquer les filtres sur les projets
        List<Project> filteredProjects = projects.stream()
                .filter(project -> projectPriority.map(p -> project.getProjectPriority() == p).orElse(true))
                .filter(project -> projectState.map(s -> project.getProjectState() == s).orElse(true))
                .filter(project -> departmentId.map(d -> project.getUser().getDepartments().stream()
                .anyMatch(department -> department.getDepartmentId().equals(d))).orElse(true))
                .filter(project -> userIds.map(
                uIds -> project.getUser().getUserId() != null && uIds.contains(project.getUser().getUserId()))
                .orElse(true))
                .filter(project -> progressRange.map(range -> {
            int start = range[0];
            int end = range[1];
            return project.getProgress() >= start && project.getProgress() <= end;
        }).orElse(true))
                .filter(project -> projectStartDate.map(s -> !project.getProjectStartDate().isBefore(s)).orElse(true))
                .filter(project -> projectEndDate.map(e -> !project.getProjectEndDate().isAfter(e)).orElse(true))
                .collect(Collectors.toList());

        // Convertir les projets filtrés en DTOs
        List<ProjectDTO> projectDTOs = filteredProjects.stream().map(this::convertToDTO).collect(Collectors.toList());

        // Utiliser le paginator pour obtenir les éléments paginés
        Page<ProjectDTO> pagedProjects = Paginator.paginate(projectDTOs, page, size, sortBy);

        // Créer la réponse avec les éléments paginés
        return new ProjectResponse(pagedProjects.getContent(), pagedProjects.getTotalElements(), pagedProjects.getTotalPages());
    }

    public List<User> getProjectUsersById(Long projectId) {

        List<User> users =userProjectRepository.findUsersByProjectId(projectId);
            if(users==null){
            throw new EntityNotFoundException("Aucun resulta trouver avec l'id : " + projectId);
            }
        return users;

    }

    public List<ProjectDTO> findFilteredProjects3(
        Optional<Priority> projectPriority,
        Optional<State> projectState,
        Optional<Long> departmentId,
        Optional<List<Long>> userIds,
        Optional<int[]> progressRange,
        Optional<LocalDateTime> projectStartDate,
        Optional<LocalDateTime> projectEndDate,
        String token,
        int page,
        int size) {

    UserRoleDTO userRoleDTO = userService.getUserRoleAndIdFromToken(token);
    Long userId = userRoleDTO.getUserId();
    Role role = userRoleDTO.getRole();

    List<Project> projects;

    if (role == Role.USER) {
        projects = projectRepository.findProjectsByUserId(userId);
    } else {
        projects = projectRepository.findAll();
    }

    // Appliquer les filtres sur les projets
    List<Project> filteredProjects = projects.stream()
        .filter(project -> projectPriority.map(p -> project.getProjectPriority() == p).orElse(true))
        .filter(project -> projectState.map(s -> project.getProjectState() == s).orElse(true))
        .filter(project -> departmentId.map(d -> project.getUser().getDepartments().stream()
                .anyMatch(department -> department.getDepartmentId().equals(d))).orElse(true))
        .filter(project -> userIds.map(
                uIds -> project.getUser().getUserId() != null && uIds.contains(project.getUser().getUserId()))
                .orElse(true))
        .filter(project -> progressRange.map(range -> {
            int start = range[0];
            int end = range[1];
            return project.getProgress() >= start && project.getProgress() <= end;
        }).orElse(true))
        .filter(project -> projectStartDate.map(s -> !project.getProjectStartDate().isBefore(s)).orElse(true))
        .filter(project -> projectEndDate.map(e -> !project.getProjectEndDate().isAfter(e)).orElse(true))
        .collect(Collectors.toList());

    // Appliquer la pagination
    int fromIndex = Math.min(page * size, filteredProjects.size());
    int toIndex = Math.min(fromIndex + size, filteredProjects.size());
    List<Project> paginatedProjects = filteredProjects.subList(fromIndex, toIndex);

    // Convertit les projets filtrés et paginés en DTOs
    return paginatedProjects.stream().map(this::convertToDTO).collect(Collectors.toList());
}


    public List<ProjectDTO> searchProjectsByName(
        String projectName,
        String token,
        int page,
        int size) {

        UserRoleDTO userRoleDTO = userService.getUserRoleAndIdFromToken(token);
        Long userId = userRoleDTO.getUserId();
        Role role = userRoleDTO.getRole();

        List<Project> projects;

        if (role == Role.USER) {
            // Si le rôle est "User", obtenir les projets assignés à l'utilisateur
            projects = projectRepository.findProjectsByUserId(userId);
        } else {
            // Sinon, obtenir tous les projets
            projects = projectRepository.findAll();
        }

        // Appliquer le filtre par nom de projet
        List<Project> filteredProjects = projects.stream()
                .filter(project -> projectName == null || project.getProjectName().toLowerCase().contains(projectName.toLowerCase()))
                .collect(Collectors.toList());

        // Appliquer la pagination
        int fromIndex = Math.min(page * size, filteredProjects.size());
        int toIndex = Math.min(fromIndex + size, filteredProjects.size());
        List<Project> paginatedProjects = filteredProjects.subList(fromIndex, toIndex);

        // Convertir les projets filtrés et paginés en DTOs
        return paginatedProjects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<ProjectDTO> findFilteredProjects1(
            Optional<Priority> projectPriority,
            Optional<State> projectState,
            Optional<Long> departmentId,
            Optional<List<Long>> userIds, // Liste des IDs d'utilisateur
            Optional<Integer> progress,
            Optional<LocalDateTime> projectStartDate,
            Optional<LocalDateTime> projectEndDate,
            String token) {

        UserRoleDTO userRoleDTO = userService.getUserRoleAndIdFromToken(token);
        Long userId = userRoleDTO.getUserId();
        Role role = userRoleDTO.getRole();

        List<Project> projects;

        if (role == Role.USER) {
            // Si le rôle est "User", obtenir les projets assignés à l'utilisateur
            projects = projectRepository.findProjectsByUserId(userId);
        } else {
            // Sinon, obtenir tous les projets
            projects = projectRepository.findAll();
        }

        // Appliquer les filtres sur les projets
        List<Project> filteredProjects = projects.stream()
                // Filtre par priorité si elle est spécifiée
                .filter(project -> projectPriority.map(p -> project.getProjectPriority() == p).orElse(true))
                // Filtre par état si il est spécifié
                .filter(project -> projectState.map(s -> project.getProjectState() == s).orElse(true))
                // Filtre par département si il est spécifié
                .filter(project -> departmentId.map(d -> project.getUser().getDepartments().stream()
                        .anyMatch(department -> department.getDepartmentId().equals(d))).orElse(true))
                // Filtre par utilisateur si il est spécifié
                .filter(project -> userIds.map(
                        uIds -> project.getUser().getUserId() != null && uIds.contains(project.getUser().getUserId()))
                        .orElse(true))
                // Filtre par progression si elle est spécifiée
                .filter(project -> progress.map(p -> project.getProgress() == p).orElse(true))
                // Filtre par date de début si elle est spécifiée
                .filter(project -> projectStartDate.map(s -> !project.getProjectStartDate().isBefore(s)).orElse(true))
                // Filtre par date de fin si elle est spécifiée
                .filter(project -> projectEndDate.map(e -> !project.getProjectEndDate().isAfter(e)).orElse(true))
                .collect(Collectors.toList()); // Collecte les projets filtrés en liste

        // Convertit les projets filtrés en DTOs
        return filteredProjects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // statistique des projet en fonction de l'utilisateur connecter :
    public Map<String, Long> getProjectStatistics(String token) {
        UserRoleDTO userRoleDTO = userService.getUserRoleAndIdFromToken(token);

        Long userId = userRoleDTO.getUserId();
        Role role = userRoleDTO.getRole();

        List<Project> projects;

        if (role == Role.USER) {
            // Si le rôle est "User", obtenir les projets assignés à l'utilisateur
            projects = projectRepository.findProjectsByUserId(userId);
        } else {
            // Sinon, obtenir tous les projets
            projects = projectRepository.findAll();
        }

        long totalProjects = projects.size();
        long inProgressProjects = projects.stream().filter(p -> p.getProjectState() == State.EN_COURS).count();
        long pendingProjects = projects.stream().filter(p -> p.getProjectState() == State.EN_ATTENTE).count();
        long completedProjects = projects.stream().filter(p -> p.getProjectState() == State.TERMINER).count();

        Map<String, Long> statistics = new HashMap<>();
        statistics.put("totalProjects", totalProjects);
        statistics.put("inProgressProjects", inProgressProjects);
        statistics.put("pendingProjects", pendingProjects);
        statistics.put("completedProjects", completedProjects);

        return statistics;
    }

    public List<ProjectDTO> findFilteredProjects0(
            Optional<Priority> projectPriority,
            Optional<State> projectState,
            Optional<Long> departmentId,
            Optional<List<Long>> userIds, // Changer en Optional<List<Long>>
            Optional<Integer> progress,
            Optional<LocalDateTime> projectStartDate,
            Optional<LocalDateTime> projectEndDate) {

        try {
            List<Project> projects = projectRepository.findAll().stream()
                    // Filtre par priorité si elle est spécifiée
                    .filter(project -> projectPriority.map(p -> project.getProjectPriority() == p).orElse(true))
                    // Filtre par état si il est spécifié
                    .filter(project -> projectState.map(s -> project.getProjectState() == s).orElse(true))
                    // Filtre par département si il est spécifié
                    .filter(project -> departmentId.map(d -> project.getUser().getDepartments().stream()
                    .anyMatch(department -> department.getDepartmentId().equals(d))).orElse(true))
                    // Filtre par utilisateur si il est spécifié
                    .filter(project -> userIds.map(uIds -> project.getUser().getUserId() != null && uIds.contains(project.getUser().getUserId())).orElse(true))
                    // Filtre par progression si elle est spécifiée
                    .filter(project -> progress.map(p -> project.getProgress() == p).orElse(true))
                    // Filtre par date de début si elle est spécifiée
                    .filter(project -> projectStartDate.map(s -> !project.getProjectStartDate().isBefore(s)).orElse(true))
                    // Filtre par date de fin si elle est spécifiée
                    .filter(project -> projectEndDate.map(e -> !project.getProjectEndDate().isAfter(e)).orElse(true))
                    .collect(Collectors.toList()); // Collecte les projets filtrés en liste

            // Convertit les projets filtrés en DTOs
            return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            // Gère les exceptions en retournant un message d'erreur
            throw new RuntimeException("Erreur lors de la récupération des projets filtrés: " + e.getMessage());
        }
    }



    public List<ProjectWithTasksDTO> getProjectsWithTasksByUser(Long userId) {
        List<Project> projects = projectRepository.findAllProjectsWithTasksByUserId(userId);
    
        return projects.stream().map(project -> {
            List<Task> tasks = taskRepository.findByProject(project).stream()
                .filter(task -> !task.getTaskState().equals(State.TERMINER)) // Filtrer les tâches
                .collect(Collectors.toList());
    
            ProjectWithTasksDTO dto = new ProjectWithTasksDTO();
            dto.setProjectId(project.getProjectId());
            dto.setProjectName(project.getProjectName());
            dto.setProjectCodes(project.getProjectCodes());
            dto.setTasks(tasks);
            return dto;
        }).collect(Collectors.toList());
    }
    

    // public List<ProjectWithTasksDTO> getProjectsWithTasksByUser2(Long userId) {
    //     List<Project> projects = projectRepository.findAllProjectsWithTasksByUserId(userId);

    //     return projects.stream().map(project -> {
    //         List<Task> tasks = taskRepository.findByProject(project);
    //         ProjectWithTasksDTO dto = new ProjectWithTasksDTO();
    //         dto.setProjectId(project.getProjectId());
    //         dto.setProjectName(project.getProjectName());
    //         dto.setProjectCodes(project.getProjectCodes());
    //         dto.setTasks(tasks);
    //         return dto;
    //     }).collect(Collectors.toList());
    // }

}