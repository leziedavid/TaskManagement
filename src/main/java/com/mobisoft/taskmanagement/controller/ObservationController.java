package com.mobisoft.taskmanagement.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.dto.ObservationDTO;
import com.mobisoft.taskmanagement.service.ObservationService;

import jakarta.ws.rs.core.MediaType;

@RestController
@Validated
@RequestMapping("/api/v1")
public class ObservationController {

    @Autowired
    private ObservationService observationService;

    @PostMapping(value = "/obs/AddObservation", consumes = MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity<BaseResponse<ObservationDTO>> AddObservation(@ModelAttribute ObservationDTO observationDTO) {
        ObservationDTO createdObservation = observationService.AddObservation(observationDTO);
        // Création de la réponse à retourner
        BaseResponse<ObservationDTO> response = new BaseResponse<>(201, "Observation créée avec succès", createdObservation);
        // Retourner la réponse avec le statut HTTP 201 Created
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping("/obs/{id}")
    public ResponseEntity<BaseResponse<ObservationDTO>> getObservationById(@PathVariable Long id) {
        ObservationDTO observation = observationService.getObservationById(id);
        BaseResponse<ObservationDTO> response = new BaseResponse<>(200, "Détails de l'observation", observation);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/obs/getObservationEndFilesById/{id}")
    public ResponseEntity<BaseResponse<ObservationDTO>> getObservationEndFilesById(@PathVariable Long id) {
        ObservationDTO observation = observationService.getObservationEndFilesById(id);
        BaseResponse<ObservationDTO> response = new BaseResponse<>(200, "Détails de l'observation", observation);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/obs/getAllObservations")
        public ResponseEntity<BaseResponse<List<ObservationDTO>>> getAllObservations() {
        List<ObservationDTO> observations = observationService.findAllObservations();
        BaseResponse<List<ObservationDTO>> response = new BaseResponse<>(200, "Liste des observations", observations);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @PutMapping(value="/obs/updateObservation/{id}", consumes = MediaType.MULTIPART_FORM_DATA)
    public ResponseEntity<BaseResponse<ObservationDTO>> updateObservation(@PathVariable Long id, @ModelAttribute  ObservationDTO observationDTO) {
        ObservationDTO updatedObservation = observationService.updateObservation(id, observationDTO);
    BaseResponse<ObservationDTO> response = new BaseResponse<>(200, "Observation mise à jour avec succès", updatedObservation);
    return new ResponseEntity<>(response, HttpStatus.OK);
}
    
    
    @DeleteMapping("/obs/delete/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteObservation(@PathVariable Long id) {
        observationService.deleteObservation(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Observation supprimée avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    
}
