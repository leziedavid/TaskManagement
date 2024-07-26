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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.dto.ObservationDTO;
import com.mobisoft.taskmanagement.service.ObservationService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;

@RestController
@Validated
@RequestMapping("/api/v1/obs")
public class ObservationController {

    @Autowired
    private ObservationService observationService;

    @ApiImplicitParams({
        @ApiImplicitParam(name = "file", dataType = "file", paramType = "form", value = "Fichier à télécharger", required = true)
    })

    @PostMapping(value = "/addObservations")
    public ResponseEntity<BaseResponse<ObservationDTO>> createObservation( @Validated @RequestBody ObservationDTO observationDTO, @RequestParam(value = "file", required = false) List<MultipartFile> files) {
        ObservationDTO createdObservation;
        
        if (files != null && !files.isEmpty()) {
            createdObservation = observationService.createObservation(observationDTO, files);
        } else {
            createdObservation = observationService.createObservation(observationDTO, null);
        }
        
        BaseResponse<ObservationDTO> response = new BaseResponse<>(HttpStatus.CREATED.value(), "Observation créée avec succès", createdObservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
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

    @PutMapping(value = "/updateObservation/{id}")
    public ResponseEntity<BaseResponse<ObservationDTO>> updateObservation(
        @PathVariable("id") Long observationId,
        @Validated @ModelAttribute ObservationDTO observationDTO,
        @RequestParam(value = "files", required = false) List<MultipartFile> files) {
    
        ObservationDTO updatedObservation;
    
        if (files != null && !files.isEmpty()) {
            updatedObservation = observationService.updateObservation(observationId, observationDTO, files);
        } else {
            updatedObservation = observationService.updateObservation(observationId, observationDTO, null);
        }
    
        BaseResponse<ObservationDTO> response = new BaseResponse<>(HttpStatus.OK.value(), "Observation mise à jour avec succès", updatedObservation);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteObservation(@PathVariable Long id) {
        observationService.deleteObservation(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Observation supprimée avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    
}
