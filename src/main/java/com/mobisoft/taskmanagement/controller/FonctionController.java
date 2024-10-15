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
import org.springframework.web.bind.annotation.RestController;

import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.dto.FonctionDTO;
import com.mobisoft.taskmanagement.service.FonctionService;

@RestController
@Validated
@RequestMapping("/api/v1")
public class FonctionController {

    @Autowired
    private FonctionService fonctionService;

    @PostMapping("/fonctions/addFonction")
    public ResponseEntity<BaseResponse<FonctionDTO>> addFonction(@Validated @RequestBody FonctionDTO fonctionDTO) {
        FonctionDTO createdFonction = fonctionService.createFonction(fonctionDTO);
        BaseResponse<FonctionDTO> response = new BaseResponse<>(201, "Fonction créée avec succès", createdFonction);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/fonctions/getFonctionById/{id}")
    public ResponseEntity<BaseResponse<FonctionDTO>> getFonctionById(@PathVariable Long id) {
        FonctionDTO fonction = fonctionService.getFonctionById(id);
        BaseResponse<FonctionDTO> response = new BaseResponse<>(200, "Détails de la fonction", fonction);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/fonctions/getAllFonctions")
    public ResponseEntity<BaseResponse<List<FonctionDTO>>> getAllFonctions() {
        List<FonctionDTO> fonctions = fonctionService.getAllFonctions();
        BaseResponse<List<FonctionDTO>> response = new BaseResponse<>(200, "Liste des fonctions", fonctions);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/fonctions/updateFonction/{id}")
    public ResponseEntity<BaseResponse<FonctionDTO>> updateFonction(@PathVariable Long id, @Validated @RequestBody FonctionDTO fonctionDTO) {
        FonctionDTO updatedFonction = fonctionService.updateFonction(id, fonctionDTO);
        BaseResponse<FonctionDTO> response = new BaseResponse<>(200, "Fonction mise à jour avec succès", updatedFonction);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/fonctions/deleteFonction/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteFonction(@PathVariable Long id) {
        fonctionService.deleteFonction(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Fonction supprimée avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
