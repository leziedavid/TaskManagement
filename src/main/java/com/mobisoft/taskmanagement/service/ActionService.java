package com.mobisoft.taskmanagement.service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.taskmanagement.dto.ActionDTO;
import com.mobisoft.taskmanagement.entity.Action;
import com.mobisoft.taskmanagement.entity.Task;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.repository.ActionRepository;
import com.mobisoft.taskmanagement.repository.TaskRepository;
import com.mobisoft.taskmanagement.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ActionService {

    @Autowired
    private ActionRepository actionRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public ActionDTO createAction(ActionDTO actionDTO) {
        Action action = convertToEntity(actionDTO);
        Action savedAction = actionRepository.save(action);
        return convertToDTO(savedAction);
    }

    public ActionDTO getActionById(Long actionId) {
        Optional<Action> optionalAction = actionRepository.findById(actionId);
        return optionalAction.map(this::convertToDTO).orElseThrow(() -> new EntityNotFoundException("Aucune action trouvée avec l'ID: " + actionId));
    }

    public List<ActionDTO> findAllActions() {
        List<Action> actions = actionRepository.findAll();
        return actions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ActionDTO updateAction(Long actionId, ActionDTO actionDTO) {
        Action action = actionRepository.findById(actionId).orElseThrow(() -> new EntityNotFoundException("L'action avec l'ID spécifié n'existe pas"));
        updateActionFromDTO(action, actionDTO);
        Action updatedAction = actionRepository.save(action);
        return convertToDTO(updatedAction);
    }

    public boolean deleteAction(Long actionId) {
        if (!actionRepository.existsById(actionId)) {
            throw new EntityNotFoundException("L'action avec l'ID spécifié n'existe pas");
        }
        actionRepository.deleteById(actionId);
        return true;
    }

    // Méthode pour obtenir les actions par task_id
    public List<ActionDTO> getActionsByTaskId(Long taskId) {
        List<Action> actions = actionRepository.findByTask_TaskId(taskId);
        return actions.stream().map(this::convertToDTO).collect(Collectors.toList());
    }



    private ActionDTO convertToDTO(Action action) {
        ActionDTO actionDTO = new ActionDTO();
        actionDTO.setActionId(action.getActionId());
        actionDTO.setLibelle(action.getLibelle());
        actionDTO.setDescription(action.getDescription());
        actionDTO.setNombreJours(action.getNombreJours());
        actionDTO.setHours(action.getHours());
        actionDTO.setTaskId(action.getTask().getTaskId());
        actionDTO.setUserId(action.getUser().getUserId());
        actionDTO.setIsValides(action.getIsValides());

        LocalDateTime ActionStartDate = action.getActionStartDate();
        String ActionStartDateAsString = ActionStartDate.toString();
        actionDTO.setActionStartDate(ActionStartDateAsString);

        LocalDateTime ActionEndDate = action.getActionEndDate();
        String ActionEndDateAsString = ActionEndDate.toString();
        actionDTO.setActionEndDate(ActionEndDateAsString);

        OffsetDateTime actionCreateDate = action.getActionCreatedAt();
        String actionCreateAsString = actionCreateDate.toString();
        actionDTO.setActionCreatedAt(actionCreateAsString);

        OffsetDateTime actionUpdatedAt = action.getActionUpdatedAt();
        String actionUpdatedAtAsString = actionUpdatedAt.toString();
        actionDTO.setActionUpdatedAt(actionUpdatedAtAsString);

        return actionDTO;
    }

    private Action convertToEntity(ActionDTO actionDTO) {

        Action action = new Action();
        action.setDescription(actionDTO.getDescription());
        action.setHours(actionDTO.getHours());
        action.setNombreJours(actionDTO.getNombreJours());
        action.setLibelle(actionDTO.getLibelle());

        LocalDateTime ActionStartDate = LocalDateTime.parse(actionDTO.getActionStartDate());
        action.setActionStartDate(ActionStartDate);

        LocalDateTime ActionEndDate = LocalDateTime.parse(actionDTO.getActionEndDate());
        action.setActionEndDate(ActionEndDate);

        if (actionDTO.getTaskId() != null) {
            Task task = taskRepository.findById(actionDTO.getTaskId()).orElseThrow(() -> new EntityNotFoundException("La tâche avec l'ID spécifié n'existe pas"));
            action.setTask(task);
        }

        if (actionDTO.getUserId() != null) {
            User user = userRepository.findById(actionDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas"));
            action.setUser(user);
        }

        return action;
    }

    private void updateActionFromDTO(Action action, ActionDTO actionDTO) {

        action.setDescription(actionDTO.getDescription());
        action.setHours(actionDTO.getHours());
        action.setNombreJours(actionDTO.getNombreJours());
        action.setLibelle(actionDTO.getLibelle());

        LocalDateTime ActionStartDate = LocalDateTime.parse(actionDTO.getActionStartDate());
        action.setActionStartDate(ActionStartDate);

        LocalDateTime ActionEndDate = LocalDateTime.parse(actionDTO.getActionEndDate());
        action.setActionEndDate(ActionEndDate);

        if (actionDTO.getTaskId() != null) {
            Task task = taskRepository.findById(actionDTO.getTaskId()).orElseThrow(() -> new EntityNotFoundException("La tâche avec l'ID spécifié n'existe pas"));
            action.setTask(task);
        }

        if (actionDTO.getUserId() != null) {
            User user = userRepository.findById(actionDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas"));
            action.setUser(user);
        }
    }

    public ActionDTO updateActionStatus(Long actionId, Integer isValides) {
        Action action = actionRepository.findById(actionId).orElseThrow(() -> new RuntimeException("Action non trouvée"));
        // System.out.println(action);
        action.setIsValides(isValides);
        Action updatedAction = actionRepository.save(action);
        return convertToDTO(updatedAction);
    }


    // Méthode pour mettre à jour le statut d'une action et recalculer le pourcentage des actions validées

    public ActionDTO updateActionStatus2(Long actionId, Integer isValides, String taskId) {
        // Trouver l'action par son ID
        Action action = actionRepository.findById(actionId).orElseThrow(() -> new RuntimeException("Action non trouvée"));
    
        // Trouver la tâche par son code
        Optional<Task> optionalTaskByCode = taskRepository.findByTaskCode(taskId);
        Action updatedAction = null; // Déclaration de la variable en dehors du bloc
    
        if (optionalTaskByCode.isPresent()) {
            Task task = optionalTaskByCode.get();
            
            // Assure-toi que la progression n'est pas nulle
            if (task.getProgression() == null) {
                task.setProgression(0); // Initialise la progression à 0 si elle est nulle
            }
    
            // Trouver toutes les actions assignées à la tâche spécifiée
            List<Action> actions = actionRepository.findByTask_TaskId(task.getTaskId());
            
            // Calculer le pourcentage d'actions validées
            long totalActions = actions.size();
            long validActions = actions.stream().filter(a -> a.getIsValides() != null && a.getIsValides() > 0).count();
            double calculatedPercentage = (totalActions > 0) ? (validActions * 100.0) / totalActions : 0.0;
    
            // Retirer ou ajouter le pourcentage en fonction de la valeur de isValides
            if (isValides == 0) {
                // Si isValides est 0, retirer le pourcentage associé à l'action
                task.setProgression(task.getProgression() - (int) Math.round((100.0 / totalActions)));
            } else if (isValides == 1) {
                // Si isValides est 1, ajouter le pourcentage associé à l'action
                task.setProgression(task.getProgression() + (int) Math.round((100.0 / totalActions)));
            }
    
            // Assurons-nous que la progression reste dans les limites de 0 à 100
            task.setProgression(Math.max(0, Math.min(100, task.getProgression())));
    
            // Sauvegarder les modifications de la tâche
            taskRepository.save(task);
    
            // Mettre à jour le statut de l'action
            action.setIsValides(isValides);
            updatedAction = actionRepository.save(action); // Initialisation de la variable
        }
    
        // Convertir l'action mise à jour en DTO et retourner
        return convertToDTO(updatedAction);
    }
    

}
