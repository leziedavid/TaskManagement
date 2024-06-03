package com.mobisoft.taskmanagement.service;

import com.mobisoft.taskmanagement.dto.ActionDTO;
import com.mobisoft.taskmanagement.entity.Action;
import com.mobisoft.taskmanagement.entity.Task;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.repository.ActionRepository;
import com.mobisoft.taskmanagement.repository.TaskRepository;
import com.mobisoft.taskmanagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        return optionalAction.map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Aucune action trouvée avec l'ID: " + actionId));
    }

    public List<ActionDTO> findAllActions() {
        List<Action> actions = actionRepository.findAll();
        return actions.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ActionDTO updateAction(Long actionId, ActionDTO actionDTO) {
        Action action = actionRepository.findById(actionId)
                .orElseThrow(() -> new EntityNotFoundException("L'action avec l'ID spécifié n'existe pas"));
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

    private ActionDTO convertToDTO(Action action) {
        ActionDTO actionDTO = new ActionDTO();
        actionDTO.setActionId(action.getTaskActionId());
        actionDTO.setDescription(action.getDescription());
        actionDTO.setHours(action.getHours());
        actionDTO.setTaskId(action.getTask().getTaskId());
        actionDTO.setUserId(action.getUser().getUserId());
        return actionDTO;
    }

    private Action convertToEntity(ActionDTO actionDTO) {
        Action action = new Action();
        action.setDescription(actionDTO.getDescription());
        action.setHours(actionDTO.getHours());

        if (actionDTO.getTaskId() != null) {
            Task task = taskRepository.findById(actionDTO.getTaskId())
                    .orElseThrow(() -> new EntityNotFoundException("La tâche avec l'ID spécifié n'existe pas"));
            action.setTask(task);
        }

        if (actionDTO.getUserId() != null) {
            User user = userRepository.findById(actionDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas"));
            action.setUser(user);
        }

        return action;
    }

    private void updateActionFromDTO(Action action, ActionDTO actionDTO) {
        action.setDescription(actionDTO.getDescription());
        action.setHours(actionDTO.getHours());

        if (actionDTO.getTaskId() != null) {
            Task task = taskRepository.findById(actionDTO.getTaskId())
                    .orElseThrow(() -> new EntityNotFoundException("La tâche avec l'ID spécifié n'existe pas"));
            action.setTask(task);
        }

        if (actionDTO.getUserId() != null) {
            User user = userRepository.findById(actionDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas"));
            action.setUser(user);
        }
    }
}
