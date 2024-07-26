package com.mobisoft.taskmanagement.service;


import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobisoft.taskmanagement.dto.ProjectDTO;
import com.mobisoft.taskmanagement.dto.ProjetDetailsDTO;
import com.mobisoft.taskmanagement.dto.ProjetUsersDTO;
import com.mobisoft.taskmanagement.dto.UserProjectDTO;
import com.mobisoft.taskmanagement.entity.Priority;
import com.mobisoft.taskmanagement.entity.Project;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.entity.Task;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.entity.UserProject;
import com.mobisoft.taskmanagement.repository.ProjectRepository;
import com.mobisoft.taskmanagement.repository.TaskRepository;
import com.mobisoft.taskmanagement.repository.UserProjectRepository;

import jakarta.persistence.EntityNotFoundException;

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

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private FileStorageService fileStorageService;

    public ProjectDTO createProject(ProjectDTO projectDTO) {

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

    public List<ProjectDTO> findAllProjects() {
        try {
            List<Project> projects = projectRepository.findAll();
            return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de tous les projets: " + e.getMessage());
        }
    }

    public ProjectDTO updateProject(String projectId, ProjectDTO projectDTO) {

        Project project = projectRepository.findByProjectCodes(projectId);
        // System.out.println(project);

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

        LocalDate projectStartDate = LocalDate.parse(projectDTO.getProjectStartDate());
        project.setProjectStartDate(projectStartDate);
        LocalDate projectEndDate = LocalDate.parse(projectDTO.getProjectEndDate());
        
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

        LocalDate projectStartDate = project.getProjectStartDate();
        String projectStartDateAsString = projectStartDate.toString();
        projectDTO.setProjectStartDate(projectStartDateAsString);

        LocalDate projectEndDate = project.getProjectEndDate();
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

        LocalDate projectStartDate = LocalDate.parse(projectDTO.getProjectStartDate());
        project.setProjectStartDate(projectStartDate);
        LocalDate projectEndDate = LocalDate.parse(projectDTO.getProjectEndDate());
        project.setProjectEndDate(projectEndDate);
    
        User user = new User();
        user.setUserId(projectDTO.getUserId());
        project.setUser(user);

        project.setProjectNombreJours(projectDTO.getProjectNombreJours());
        project.setPrioColor(projectDTO.getPrioColor());
        project.setStateColor(projectDTO.getStateColor());
        project.setProgress(projectDTO.getProgress());
        project.setProjectUpdatedAt(OffsetDateTime.now()); // Mettre à jour la date de mise à jour

        // // Conversion des dates depuis String vers OffsetDateTime
        // OffsetDateTime projectCreatedAt = OffsetDateTime.parse(projectDTO.getProjectCreatedAt());
        // project.setProjectCreatedAt(projectCreatedAt);
        // OffsetDateTime projectUpdatedAt = OffsetDateTime.parse(projectDTO.getProjectUpdatedAt());
        // project.setProjectUpdatedAt(projectUpdatedAt);

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


}
