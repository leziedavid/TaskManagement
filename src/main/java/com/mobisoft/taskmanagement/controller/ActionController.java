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

import com.mobisoft.taskmanagement.dto.ActionDTO;
import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.service.ActionService;

@RestController
@Validated
@RequestMapping("/api/v1")
public class ActionController {

    @Autowired
    private ActionService actionService;

    @PostMapping("/actions/addTaskActions")
    public ResponseEntity<BaseResponse<ActionDTO>> addTaskActions(@Validated @RequestBody ActionDTO actionDTO) {
        ActionDTO createdAction = actionService.createAction(actionDTO);
        BaseResponse<ActionDTO> response = new BaseResponse<>(201, "Action créée avec succès", createdAction);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/actions/getActionById/{id}")
    public ResponseEntity<BaseResponse<ActionDTO>> getActionById(@PathVariable Long id) {
        ActionDTO action = actionService.getActionById(id);
        BaseResponse<ActionDTO> response = new BaseResponse<>(200, "Détails de l'action", action);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/actions/getActionsByTaskId/{id}")
    public ResponseEntity<BaseResponse<List<ActionDTO>>> getActionsByTaskId(@PathVariable Long id) {
        List<ActionDTO> actions = actionService.getActionsByTaskId(id);
        BaseResponse<List<ActionDTO>> response = new BaseResponse<>(200, "Détails des actions", actions);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/actions/getAllActions")
    public ResponseEntity<BaseResponse<List<ActionDTO>>> getAllActions() {
        List<ActionDTO> actions = actionService.findAllActions();
        BaseResponse<List<ActionDTO>> response = new BaseResponse<>(200, "Liste des actions", actions);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/actions/updateAction/{id}")
    public ResponseEntity<BaseResponse<ActionDTO>> updateAction(@PathVariable Long id, @Validated @RequestBody ActionDTO actionDTO) {
        ActionDTO updatedAction = actionService.updateAction(id, actionDTO);
        BaseResponse<ActionDTO> response = new BaseResponse<>(201, "Action mise à jour avec succès", updatedAction);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/actions/deleteAction/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteAction(@PathVariable Long id) {
        actionService.deleteAction(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Action supprimée avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/actions/update-action-status")
    public ResponseEntity<BaseResponse<ActionDTO>> updateActionStatus(
            @RequestParam Long actionId,
            @RequestParam Integer isValides,
            @RequestParam String taskId) {

        // Validation des paramètres (exemple de validation simple)
        if (actionId == null || isValides == null || taskId == null) {
            return new ResponseEntity<>(new BaseResponse<>(400, "Tous les paramètres doivent être fournis", null), HttpStatus.BAD_REQUEST);
        }

        // Appeler le service pour mettre à jour le statut de l'action
        try {
            ActionDTO updatedAction = actionService.updateActionStatus2(actionId, isValides, taskId);

            // Vérifier si la mise à jour a réussi
            if (updatedAction == null) {
                return new ResponseEntity<>(new BaseResponse<>(404, "Action non trouvée", null), HttpStatus.NOT_FOUND);
            }

            // Préparer la réponse
            BaseResponse<ActionDTO> response = new BaseResponse<>(200, "Statut de l'action mis à jour avec succès", updatedAction);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Gestion des erreurs
            return new ResponseEntity<>(new BaseResponse<>(500, "Erreur interne du serveur", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // public ResponseEntity<BaseResponse<ActionDTO>> updateActionStatus(@RequestParam Long actionId,@RequestParam Integer isValides) {
    //     // Appeler le service pour mettre à jour le statut de l'action
    //     ActionDTO updatedAction = actionService.updateActionStatus(actionId, isValides);
    //     // Préparer la réponse
    //     BaseResponse<ActionDTO> response = new BaseResponse<>(200, "Statut de l'action mis à jour avec succès", updatedAction);
    //     return new ResponseEntity<>(response, HttpStatus.OK);
    // }

    
}
