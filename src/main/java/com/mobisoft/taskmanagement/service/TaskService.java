package com.mobisoft.taskmanagement.service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.taskmanagement.dto.AbonnementDTO;
import com.mobisoft.taskmanagement.dto.ActionDTO;
import com.mobisoft.taskmanagement.dto.ObservationDTO;
import com.mobisoft.taskmanagement.dto.TaskDTO;
import com.mobisoft.taskmanagement.dto.TaskDetailsDTO;
import com.mobisoft.taskmanagement.dto.UserDTO;
import com.mobisoft.taskmanagement.dto.UserRoleDTO;
import com.mobisoft.taskmanagement.entity.Action;
import com.mobisoft.taskmanagement.entity.Priority;
import com.mobisoft.taskmanagement.entity.Project;
import com.mobisoft.taskmanagement.entity.Role;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.entity.Task;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.repository.AbonnementRepository;
import com.mobisoft.taskmanagement.repository.ActionRepository;
import com.mobisoft.taskmanagement.repository.ObservationRepository;
import com.mobisoft.taskmanagement.repository.ProjectRepository;
import com.mobisoft.taskmanagement.repository.TaskRepository;
import com.mobisoft.taskmanagement.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private ObservationRepository observationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService; // Injection de UserService

    @Autowired
    private NotificationService notificationService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS");

    @Autowired
    private AbonnementRepository abonnementRepository;

    
    public TaskDTO createTask(TaskDTO taskDTO) {

        try {

            Task task = convertToEntity(taskDTO);
            Task savedTask = taskRepository.save(task);

            // Créer une notification pour la nouvelle tâche
            String title = "Nouvelle Tâche Crée";
            String message = "Une nouvelle tâche a été crée avec l'ID: " + savedTask.getTaskId();
            Long entityId = savedTask.getTaskId();
            Long codeProject = savedTask.getProjectId();
            String entityType = "Task";

            Long addBy = savedTask.getUser().getUserId();

            Set<Long> userIds = new HashSet<>();
            // Ajoutez ici l'ID de l'utilisateur assigné à la tâche
            userIds.add(task.getAssigned().getUserId());
            notificationService.createNotificationForAction(title, message, entityId, entityType, userIds, codeProject, addBy);
            return convertToDTO(savedTask);

        } catch (Exception e) {
            throw new EntityNotFoundException("Erreur lors de la création de la tâche: " + e.getMessage());
        }
    }

    public TaskDTO updateDifficulty(String taskCode, TaskDTO taskDTO) {
        try {
            // Récupérer la tâche existante par son code
            Task existingTask = taskRepository.findByTaskCode(taskCode)
                .orElseThrow(() -> new EntityNotFoundException("Tâche non trouvée avec le code: " + taskCode));
    
            // Mettre à jour les champs difficulté et niveau
            existingTask.setDifficulte(taskDTO.getDifficulte());
            existingTask.setLevel(taskDTO.getLevel());
    
            // Enregistrer la tâche mise à jour
            Task updatedTask = taskRepository.save(existingTask);
    
            return convertToDTO(updatedTask);
        } catch (Exception e) {
            throw new EntityNotFoundException("Erreur lors de la mise à jour de la tâche: " + e.getMessage());
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

    public List<TaskDTO> getAllTasksByProjectId(String projectCodes, String token, Long Iduser) {

        UserRoleDTO userRoleDTO;
        // Obtenez les informations utilisateur à partir du token ou de userId
        if (Iduser != null) {
            userRoleDTO = userService.getUserRoleAndIdFromUserId(Iduser);
        } else {
            userRoleDTO = userService.getUserRoleAndIdFromToken(token);
        }

        // Utilisez l'ID et le rôle de l'utilisateur comme nécessaire
        Long userId = userRoleDTO.getUserId();
        Role role = userRoleDTO.getRole();

        // System.out.println(userId);
        Project project = projectRepository.findByProjectCodes(projectCodes);

        if (project == null) {
            throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + projectCodes);
        }

        Long id = project.getProjectId();

        List<Task> tasks = taskRepository.findTasksByProjectId(id);

        if (tasks.isEmpty()) {

            throw new EntityNotFoundException("Aucune tâche trouvée pour le projet avec l'ID: " + id);
        }
        return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<TaskDTO> findAllTasks() {
        try {
            List<Task> tasks = taskRepository.findAll();
            return tasks.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de toutes les tâches: " + e.getMessage());
        }
    }

    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("La tâche avec l'ID spécifié n'existe pas"));
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
    public TaskDTO updateTaskStateAndColors2(Long taskId, State newState, String selectedColors) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Le projet avec l'ID spécifié n'existe pas"));
        task.setTaskState(newState);
        task.setIsValides(0);
        task.setStateColor(selectedColors);
        task.setTaskUpdatedAt(OffsetDateTime.now());

        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    public TaskDTO updateTaskStateAndColors(Long taskId, State newState, String selectedColors,String projetsId,Long steps) {
        // Récupération de la tâche
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("La tâche avec l'ID spécifié n'existe pas"));
        // Vérification des actions associées
        List<Action> actions = actionRepository.findByTask_TaskId(taskId);
        boolean allActionsValid = actions.stream().allMatch(action -> action.getIsValides() != 0);

        if (!allActionsValid) {
            throw new IllegalStateException("Toutes les actions associées doivent avoir un statut 'is_valides' différent de 0.");
        }

        // Mise à jour de la priorité et de la couleur
        if (newState == State.TERMINER) { // Utilise l'énumération ici
            task.setProgression(100);
        }

        if (steps == 5) {
            Project project = projectRepository.findByProjectCodes(projetsId);
        
            if (project == null) {
                throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + projetsId);
            }
        
            // Diminue le pourcentage d'avancement de 1, en s'assurant qu'il ne soit pas négatif
            int currentProgress = project.getProgress();
            if (currentProgress > 0) {
                project.setProgress(currentProgress - 1);
            } else {
                throw new IllegalArgumentException("Le pourcentage d'avancement ne peut pas être négatif.");
            }
        
            // Sauvegarder les modifications du projet
            projectRepository.save(project);
        }

        task.setTaskState(newState);
        task.setIsValides(0);
        task.setStateColor(selectedColors);
        task.setTaskUpdatedAt(OffsetDateTime.now());
        // Sauvegarde de la tâche mise à jour
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    // Méthode pour mettre à jour du status et la couleur d'un projet
    public TaskDTO validteTaskState2(Long taskId, Integer newState, String selectedColors, String projetsId) {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Le projet avec l'ID spécifié n'existe pas"));
        // Mise à jour de la priorité et de la couleur
        task.setIsValides(newState);
        // task.setTaskState(newState);
        task.setStateColor(selectedColors);
        task.setTaskUpdatedAt(OffsetDateTime.now());// Mettre à jour la date de mise à jour
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    // Méthode pour mettre à jour le statut et la couleur d'un projet
    public TaskDTO validteTaskState(Long taskId, Integer newState, String selectedColors, String projectCodes) {

        Project project = projectRepository.findByProjectCodes(projectCodes);

        if (project == null) {
            throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + projectCodes);
        }

        Long id = project.getProjectId();
        
        if (newState == 1) {
            project.setProjectState(State.TERMINER);
        }

        List<Task> tasks = taskRepository.findTasksByProjectId(id);

        if (tasks.isEmpty()) {
            project.setProgress(0); // Si aucune tâche, le projet est à 0% de progression
        } else {
            int totalTasks = tasks.size();
            int totalProgress = 0;

            for (Task task : tasks) {
                // Assurer que la progression de la tâche est bien définie
                if (task.getProgression() == null) {
                    task.setProgression(0); // Initialise la progression à 0 si elle est nulle
                }
                totalProgress += task.getProgression();
            }

            // Calculer le pourcentage moyen de progression des tâches
            int averageProgress = totalProgress / totalTasks;

            // Mettre à jour la progression du projet
            project.setProgress(averageProgress);
        }

        // Sauvegarder les modifications du projet
        projectRepository.save(project);

        // Trouver et mettre à jour la tâche spécifiée
        Task task = taskRepository.findById(taskId).orElseThrow(()-> new EntityNotFoundException("La tâche avec l'ID spécifié n'existe pas"));

        // Mise à jour de la priorité et de la couleur
        task.setIsValides(newState);
        task.setStateColor(selectedColors);
        task.setTaskUpdatedAt(OffsetDateTime.now()); // Mettre à jour la date de mise à jour
        Task updatedTask = taskRepository.save(task);

        return convertToDTO(updatedTask);
    }

    private TaskDTO convertToDTO(Task task) {

        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setTaskId(task.getTaskId());
        taskDTO.setIsValides(task.getIsValides());
        taskDTO.setTaskName(task.getTaskName());
        taskDTO.setTaskCode(task.getTaskCode());
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

        taskDTO.setDifficulte(task.getDifficulte());
        taskDTO.setLevel(task.getLevel());

        LocalDateTime taskStartDate = task.getTaskStartDate();
        String taskStartDateAsString = (taskStartDate != null) ? taskStartDate.toString() : null;
        taskDTO.setTaskStartDate(taskStartDateAsString);

        LocalDateTime taskEndDate = task.getTaskEndDate();
        String taskEndDateAsString = (taskEndDate != null) ? taskEndDate.toString() : null;
        taskDTO.setTaskEndDate(taskEndDateAsString);

        OffsetDateTime taskCreatedAt = task.getTaskCreatedAt();
        String taskCreatedAtAsString = (taskCreatedAt != null) ? taskCreatedAt.toString() : null;
        taskDTO.setTaskCreatedAt(taskCreatedAtAsString);

        OffsetDateTime taskUpdatedAt = task.getTaskUpdatedAt();
        String taskUpdatedAtAsString = (taskUpdatedAt != null) ? taskUpdatedAt.toString() : null;
        taskDTO.setTaskUpdatedAt(taskUpdatedAtAsString);

        LocalDateTime alerteDate = task.getAlerteDate();
        String alerteDateAsString = (alerteDate != null) ? alerteDate.toString() : null;
        taskDTO.setAlerteDate(alerteDateAsString);

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
        task.setTaskCode(CodeGenerator.generateUniqueProjectCode());
        // System.out.println(taskDTO.getProjectCodes());

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

        LocalDateTime taskStartDate = LocalDateTime.parse(taskDTO.getTaskStartDate());
        task.setTaskStartDate(taskStartDate);

        LocalDateTime taskEndDate = LocalDateTime.parse(taskDTO.getTaskEndDate());
        task.setTaskEndDate(taskEndDate);
        if (taskDTO.getAlerteDate() != null && !taskDTO.getAlerteDate().isEmpty()) {
            LocalDateTime alerteDate = LocalDateTime.parse(taskDTO.getAlerteDate(), FORMATTER);
            task.setAlerteDate(alerteDate);
        }

        return task;
    }

    private void updateTaskFromDTO(Task task, TaskDTO taskDTO) {

        // Mise à jour des attributs de la tâche
        task.setTaskName(taskDTO.getTaskName());
        task.setTaskDescription(taskDTO.getTaskDescription());
        task.setColorCode(taskDTO.getColorCode());

        // Ajouter les attributs manquants
        task.setPrioColor(taskDTO.getPrioColor());
        task.setProgression(taskDTO.getProgression());
        task.setTaskNombreHeurs(taskDTO.getTaskNombreHeurs());
        task.setTaskNombreJours(taskDTO.getTaskNombreJours());

        task.setTaskPriority(Priority.valueOf(taskDTO.getTaskPriority()));
        task.setTaskState(State.valueOf(taskDTO.getTaskState()));

        // Mise à jour du projet
        Project project = projectRepository.findByProjectCodes(taskDTO.getProjectCodes());

        if (project == null) {
            throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + taskDTO.getProjectCodes());
        }

        task.setProject(project);

        // Mise à jour de l'utilisateur
        User user = new User();
        user.setUserId(taskDTO.getUserId());
        task.setUser(user);

        // Mise à jour de l'utilisateur assigné
        User userAssigned = new User();
        userAssigned.setUserId(taskDTO.getAssigned());
        task.setAssigned(userAssigned);

        // Mise à jour des dates
        LocalDateTime taskStartDate = LocalDateTime.parse(taskDTO.getTaskStartDate());
        task.setTaskStartDate(taskStartDate);

        LocalDateTime taskEndDate = LocalDateTime.parse(taskDTO.getTaskEndDate());
        task.setTaskEndDate(taskEndDate);
        task.setTaskUpdatedAt(OffsetDateTime.now());

        // Mise à jour de la date d'alerte (ajoutée dans convertToEntity)
        if (taskDTO.getAlerteDate() != null && !taskDTO.getAlerteDate().isEmpty()) {
            LocalDateTime alerteDate = LocalDateTime.parse(taskDTO.getAlerteDate(), FORMATTER);
            task.setAlerteDate(alerteDate);
        }

    }

    // liste des taches et ses calsse enfants
    public TaskDetailsDTO getTaskDetailsByCode(String taskCode) {

        TaskDetailsDTO taskDetailsDTO = new TaskDetailsDTO();

        // Retrieve the task based on the task code
        Optional<Task> optionalTask = taskRepository.findByTaskCode(taskCode);

        if (optionalTask.isPresent()) {

            Task task = optionalTask.get();
            TaskDTO taskDTO = convertToDTO(task);

            // System.out.println(task);

            // Retrieve actions
            List<ActionDTO> actionDTOs = actionRepository.findByTaskTaskId(task.getTaskId())
                    .stream()
                    .map(action -> {
                        ActionDTO actionDTO = new ActionDTO();
                        actionDTO.setActionId(action.getActionId());
                        actionDTO.setLibelle(action.getLibelle());
                        actionDTO.setNombreJours(action.getNombreJours());
                        actionDTO.setDescription(action.getDescription());
                        actionDTO.setHours(action.getHours());
                        actionDTO.setTaskId(action.getTask().getTaskId());
                        actionDTO.setUserId(action.getUser().getUserId());
                        actionDTO.setIsValides(action.getIsValides());

                        // Convert OffsetDateTime to String
                        OffsetDateTime actionCreateDate = action.getActionCreatedAt();
                        String actionCreateAsString = actionCreateDate.toString();
                        actionDTO.setActionCreatedAt(actionCreateAsString);

                        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                        // String formattedDate = action.getActionStartDate().format(formatter);
                        // actionDTO.setActionStartDate(formattedDate);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
                        String formattedDate = action.getActionStartDate().atOffset(ZoneOffset.UTC).format(formatter) + "Z";
                        actionDTO.setActionStartDate(formattedDate);

                        LocalDateTime taskEndDate = LocalDateTime.parse(taskDTO.getTaskEndDate());
                        task.setTaskEndDate(taskEndDate);

                        OffsetDateTime actionUpdatedAt = action.getActionUpdatedAt();
                        String actionUpdatedAtAsString = actionUpdatedAt.toString();
                        actionDTO.setActionUpdatedAt(actionUpdatedAtAsString);

                        return actionDTO;
                    })
                    .limit(5) // Limit to 10
                    .collect(Collectors.toList());

            // Retrieve observations
            List<ObservationDTO> observationDTOs = observationRepository.findByTaskTaskId(task.getTaskId())
                    .stream()
                    .map(observation -> {
                        ObservationDTO observationDTO = new ObservationDTO();
                        observationDTO.setObservationId(observation.getObservationId());
                        observationDTO.setLibelle(observation.getLibelle());
                        observationDTO.setDescription(observation.getDescription());
                        observationDTO.setTaskId(observation.getTask().getTaskId());
                        observationDTO.setUserId(observation.getUser().getUserId());

                        // Convert OffsetDateTime to String
                        OffsetDateTime obCreateDate = observation.getObservationCreatedAt();
                        String obCreateDateAsString = obCreateDate.toString();
                        observationDTO.setObservationCreatedAt(obCreateDateAsString);

                        return observationDTO;
                    })
                    .limit(5) // Limit to 10
                    .collect(Collectors.toList());

            // Retrieve assigned users
            List<UserDTO> assignedUserDTOs = task.getAssigned() != null
                    ? List.of(convertToUserDTO(task.getAssigned()))
                    : List.of();


            List<AbonnementDTO> abonnementDTOs = abonnementRepository.findByIdTask(task.getTaskId())
                    .stream()
                    .map(abonnement -> {
                        AbonnementDTO abonnementDTO = new AbonnementDTO();
                        abonnementDTO.setAbonnementId(abonnement.getAbonnementId());
                        abonnementDTO.setIdTask(abonnement.getIdTask());
                        abonnementDTO.setUserId(abonnement.getUserId());
                        abonnementDTO.setEmail(abonnement.getEmail());
                        abonnementDTO.setName(abonnement.getName());
                        // abonnementDTO.setCreatedAt(abonnement.getCreatedAt().toString());
                        return abonnementDTO;
                    })
                    .collect(Collectors.toList());


            // Set DTOs
            taskDetailsDTO.setTasks(List.of(taskDTO)); // Wrap taskDTO in a list
            taskDetailsDTO.setActions(actionDTOs);
            taskDetailsDTO.setObservations(observationDTOs);
            taskDetailsDTO.setAssignedUsers(assignedUserDTOs);
            taskDetailsDTO.setAbonnements(abonnementDTOs); // Add this line to set subscriptions

        }

        return taskDetailsDTO;
    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setFonction(user.getFonction());
        return userDTO;
    }

    public TaskDTO updateAssignedUser(Long taskId, Long assignedUserId) {
        // Vérifier si la tâche existe
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task not found with id " + taskId));
        // Vérifier si l'utilisateur attribué existe
        User assignedUser = userRepository.findById(assignedUserId).orElseThrow(() -> new RuntimeException("User not found with id " + assignedUserId));
        // Mettre à jour l'utilisateur attribué pour la tâche
        task.setAssigned(assignedUser);
        Task updatedTask = taskRepository.save(task);
        return convertToDTO(updatedTask);
    }

    // Méthode pour mettre à jour la date d'alerte d'une tâche
    public TaskDTO updateTaskAlerteDate(Long taskId, String newAlerteDate) {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("La tâche avec l'ID spécifié n'existe pas"));

        // Validation et conversion de la date d'alerte
        LocalDateTime alerteDate;
        try {
            alerteDate = LocalDateTime.parse(newAlerteDate, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"));

        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Format de date invalide pour alerteDate");
        }

        // Mise à jour de la date d'alerte
        task.setAlerteDate(alerteDate);
        task.setTaskUpdatedAt(OffsetDateTime.now()); // Mettre à jour la date de mise à jour
        Task updatedTask = taskRepository.save(task);

        return convertToDTO(updatedTask);
    }

    // Méthode pour mettre à jour la priorité et la couleur d'un projet
    public TaskDTO updatePriorityAndColor(Long taskId, Priority newPriority, String selectedColors) {

        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Le projet avec l'ID spécifié n'existe pas"));
        // Mise à jour de la priorité et de la couleur
        task.setTaskPriority(newPriority);
        task.setPrioColor(selectedColors);
        task.setTaskUpdatedAt(OffsetDateTime.now()); // Mettre à jour la date de mise à jour
        Task updatedTask = taskRepository.save(task);

        return convertToDTO(updatedTask);
    }

    // Api me permetant de filtré mes tache avec l'id du projet
    public List<TaskDTO> findFilteredTasks(
            Optional<String> projectCode,
            Optional<Priority> taskPriority,
            Optional<State> taskState,
            Optional<Long> assignedUserId,
            Optional<Integer> progression,
            Optional<LocalDateTime> taskStartDate,
            Optional<LocalDateTime> taskEndDate,
            String token) {

        try {
            // Utilisation d'une classe interne pour encapsuler projectId
            final LongHolder projectIdHolder = new LongHolder();

            // Si un code de projet est fourni, obtenir l'ID du projet correspondant
            projectCode.ifPresent(code -> {
                Project project = projectRepository.findByProjectCodes(code);
                if (project == null) {
                    throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + code);
                }
                projectIdHolder.value = project.getProjectId();
            });

            UserRoleDTO userRoleDTO = userService.getUserRoleAndIdFromToken(token);
            Long userId = userRoleDTO.getUserId();
            Role role = userRoleDTO.getRole();

            // Récupérer les tâches selon le rôle de l'utilisateur
            List<Task> tasks;
            if (role == Role.USER) {
                // Si le rôle est "User", obtenir les tâches assignées à l'utilisateur
                tasks = taskRepository.findByAssigned_UserId(userId);
            } else {
                // Sinon, obtenir toutes les tâches
                tasks = taskRepository.findAll();
            }

            // Appliquer les filtres sur les tâches
            List<Task> filteredTasks = tasks.stream()
                    // Filtre par projectId si spécifié
                    .filter(task -> projectIdHolder.value == null || task.getProject().getProjectId().equals(projectIdHolder.value))
                    // Filtre par priorité si spécifiée
                    .filter(task -> taskPriority.map(p -> task.getTaskPriority() == p).orElse(true))
                    // Filtre par état si spécifié
                    .filter(task -> taskState.map(s -> task.getTaskState() == s).orElse(true))
                    // Filtre par utilisateur assigné si spécifié
                    .filter(task -> assignedUserId.map(id -> task.getAssigned() != null && task.getAssigned().getUserId().equals(id)).orElse(true))
                    // Filtre par progression si spécifiée
                    .filter(task -> progression.map(p -> task.getProgression().equals(p)).orElse(true))
                    // Filtre par date de début si spécifiée
                    .filter(task -> taskStartDate.map(date -> !task.getTaskStartDate().isBefore(date)).orElse(true))
                    // Filtre par date de fin si spécifiée
                    .filter(task -> taskEndDate.map(date -> !task.getTaskEndDate().isAfter(date)).orElse(true))
                    .collect(Collectors.toList()); // Collecte les tâches filtrées en liste

            // Convertit les tâches filtrées en DTOs
            return filteredTasks.stream().map(this::convertToDTO).collect(Collectors.toList());

        } catch (Exception e) {
            // Gère les exceptions en retournant un message d'erreur
            throw new RuntimeException("Erreur lors de la récupération des tâches filtrées: " + e.getMessage());
        }
    }

    // Classe interne pour encapsuler la valeur
    private static class LongHolder {

        private Long value;
    }

    /**
     * Met à jour la progression du projet et les détails d'une tâche.
     *
     * @param projectId ID du projet à mettre à jour
     * @param taskId ID de la tâche à mettre à jour
     * @param newState Nouveau état pour la tâche (par exemple, validé ou non)
     * @param selectedColors Couleur sélectionnée pour l'état de la tâche
     */
    public TaskDTO updateProjectAndTask(Long projectId, Long taskId, Integer newState, String selectedColors) {

        // 1. Mettre à jour la progression du projet
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Projet non trouvé"));

        List<Task> tasks = taskRepository.findTasksByProjectId(projectId);

        if (tasks.isEmpty()) {
            project.setProgress(0); // Si aucune tâche, le projet est à 0% de progression

        } else {

            int totalTasks = tasks.size();
            int totalProgress = 0;

            for (Task task : tasks) {
                // Assurer que la progression de la tâche est bien définie
                if (task.getProgression() == null) {
                    task.setProgression(0); // Initialise la progression à 0 si elle est nulle
                }
                totalProgress += task.getProgression();
            }

            // Calculer le pourcentage moyen de progression des tâches
            int averageProgress = totalProgress / totalTasks;

            // Mettre à jour la progression du projet
            project.setProgress(averageProgress);
        }

        // Sauvegarder les modifications du projet
        projectRepository.save(project);

        // 2. Mettre à jour les détails de la tâche
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Tâche non trouvée"));

        // Mise à jour de l'état et de la couleur de la tâche
        task.setIsValides(newState);
        task.setStateColor(selectedColors);
        task.setTaskUpdatedAt(OffsetDateTime.now()); // Mettre à jour la date de mise à jour

        // Sauvegarder les modifications de la tâche
        Task updatedTask = taskRepository.save(task);

        // Convertir la tâche mise à jour en TaskDTO
        return convertToDTO(updatedTask);
    }

}
