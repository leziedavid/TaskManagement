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

import com.mobisoft.taskmanagement.dto.LeaveDTO;
import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.service.LeaveService;

@RestController
@Validated
@RequestMapping("/api/v1/leave")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    @PostMapping("/addLeave")
    public ResponseEntity<BaseResponse<LeaveDTO>> addLeave(@Validated @RequestBody LeaveDTO leaveDTO) {
        LeaveDTO createdLeave = leaveService.createLeave(leaveDTO);
        BaseResponse<LeaveDTO> response = new BaseResponse<>(201, "Congé créé avec succès", createdLeave);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/getLeaveById/{id}")
    public ResponseEntity<BaseResponse<LeaveDTO>> getLeaveById(@PathVariable Long id) {
        LeaveDTO leave = leaveService.getLeaveById(id);
        BaseResponse<LeaveDTO> response = new BaseResponse<>(200, "Détails du congé", leave);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getLeavesByUserId/{userId}")
    public ResponseEntity<BaseResponse<List<LeaveDTO>>> getLeavesByUserId(@PathVariable Long userId) {
        List<LeaveDTO> leaves = leaveService.getLeavesByUserId(userId);
        BaseResponse<List<LeaveDTO>> response = new BaseResponse<>(200, "Détails des congés", leaves);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllLeaves")
    public ResponseEntity<BaseResponse<List<LeaveDTO>>> getAllLeaves() {
        List<LeaveDTO> leaves = leaveService.findAllLeaves();
        BaseResponse<List<LeaveDTO>> response = new BaseResponse<>(200, "Liste des congés", leaves);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/updateLeave/{id}")
    public ResponseEntity<BaseResponse<LeaveDTO>> updateLeave(@PathVariable Long id, @Validated @RequestBody LeaveDTO leaveDTO) {
        LeaveDTO updatedLeave = leaveService.updateLeave(id, leaveDTO);
        BaseResponse<LeaveDTO> response = new BaseResponse<>(200, "Congé mis à jour avec succès", updatedLeave);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/deleteLeave/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteLeave(@PathVariable Long id) {
        leaveService.deleteLeave(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Congé supprimé avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/update-leave-status")
    public ResponseEntity<BaseResponse<LeaveDTO>> updateLeaveStatus(
            @RequestParam Long leaveId,
            @RequestParam String status) {

        // Validation des paramètres
        if (leaveId == null || status == null) {
            return new ResponseEntity<>(new BaseResponse<>(400, "Tous les paramètres doivent être fournis", null), HttpStatus.BAD_REQUEST);
        }

        // Appeler le service pour mettre à jour le statut du congé
        try {
            LeaveDTO updatedLeave = leaveService.updateLeaveStatus(leaveId, status);

            // Vérifier si la mise à jour a réussi
            if (updatedLeave == null) {
                return new ResponseEntity<>(new BaseResponse<>(404, "Congé non trouvé", null), HttpStatus.NOT_FOUND);
            }

            // Préparer la réponse
            BaseResponse<LeaveDTO> response = new BaseResponse<>(200, "Statut du congé mis à jour avec succès", updatedLeave);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            // Gestion des erreurs
            return new ResponseEntity<>(new BaseResponse<>(500, "Erreur interne du serveur", null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
