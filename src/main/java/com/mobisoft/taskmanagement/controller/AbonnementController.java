package com.mobisoft.taskmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.mobisoft.taskmanagement.dto.AbonnementDTO;
import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.service.AbonnementService;

@RestController
@RequestMapping("/api/v1")
public class AbonnementController {

    @Autowired
    private AbonnementService abonnementService;

    @PostMapping("/abonnements/{taskId}")
    public ResponseEntity<BaseResponse<List<AbonnementDTO>>> createAbonnement(
            @PathVariable Long taskId,
            @RequestBody AbonnementDTO abonnementDTO) {
        
        // Vérifiez que l'ID de la tâche est valide
        if (taskId == null || taskId <= 0) {
            BaseResponse<List<AbonnementDTO>> response = new BaseResponse<>(400, "ID de la tâche invalide", null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        List<AbonnementDTO> createdAbonnements = abonnementService.createAbonnement(taskId, abonnementDTO);
        BaseResponse<List<AbonnementDTO>> response = new BaseResponse<>(200, "Abonnements créés avec succès", createdAbonnements);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}

