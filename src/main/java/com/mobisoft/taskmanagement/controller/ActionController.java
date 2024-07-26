package com.mobisoft.taskmanagement.controller;

import com.mobisoft.taskmanagement.dto.ActionDTO;
import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.service.ActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/actions")
public class ActionController {

    @Autowired
    private ActionService actionService;

    @PostMapping
    public ResponseEntity<BaseResponse<ActionDTO>> createAction(@Validated @RequestBody ActionDTO actionDTO) {
        ActionDTO createdAction = actionService.createAction(actionDTO);
        BaseResponse<ActionDTO> response = new BaseResponse<>(201, "Action créée avec succès", createdAction);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ActionDTO>> getActionById(@PathVariable Long id) {
        ActionDTO action = actionService.getActionById(id);
        BaseResponse<ActionDTO> response = new BaseResponse<>(200, "Détails de l'action", action);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllActions")
    public ResponseEntity<BaseResponse<List<ActionDTO>>> getAllActions() {
        List<ActionDTO> actions = actionService.findAllActions();
        BaseResponse<List<ActionDTO>> response = new BaseResponse<>(200, "Liste des actions", actions);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ActionDTO>> updateAction(@PathVariable Long id, @Validated @RequestBody ActionDTO actionDTO) {
        ActionDTO updatedAction = actionService.updateAction(id, actionDTO);
        BaseResponse<ActionDTO> response = new BaseResponse<>(200, "Action mise à jour avec succès", updatedAction);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteAction(@PathVariable Long id) {
        actionService.deleteAction(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Action supprimée avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
