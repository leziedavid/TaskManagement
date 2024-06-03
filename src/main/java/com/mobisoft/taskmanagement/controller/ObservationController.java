package com.mobisoft.taskmanagement.controller;

import com.mobisoft.taskmanagement.dto.ObservationDTO;
import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.service.ObservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/observations")
public class ObservationController {

    @Autowired
    private ObservationService observationService;

    @PostMapping
    public ResponseEntity<BaseResponse<ObservationDTO>> createObservation(@Validated @RequestBody ObservationDTO observationDTO) {
        ObservationDTO createdObservation = observationService.createObservation(observationDTO);
        BaseResponse<ObservationDTO> response = new BaseResponse<>(201, "Observation créée avec succès", createdObservation);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ObservationDTO>> getObservationById(@PathVariable Long id) {
        ObservationDTO observation = observationService.getObservationById(id);
        BaseResponse<ObservationDTO> response = new BaseResponse<>(200, "Détails de l'observation", observation);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllObservations")
    public ResponseEntity<BaseResponse<List<ObservationDTO>>> getAllObservations() {
        List<ObservationDTO> observations = observationService.findAllObservations();
        BaseResponse<List<ObservationDTO>> response = new BaseResponse<>(200, "Liste des observations", observations);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ObservationDTO>> updateObservation(@PathVariable Long id,@Validated @RequestBody ObservationDTO observationDTO) {
        ObservationDTO updatedObservation = observationService.updateObservation(id, observationDTO);
        BaseResponse<ObservationDTO> response = new BaseResponse<>(200, "Observation mise à jour avec succès", updatedObservation);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteObservation(@PathVariable Long id) {
        observationService.deleteObservation(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Observation supprimée avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
