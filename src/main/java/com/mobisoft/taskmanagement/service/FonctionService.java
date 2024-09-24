package com.mobisoft.taskmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.taskmanagement.dto.FonctionDTO;
import com.mobisoft.taskmanagement.entity.Fonction;
import com.mobisoft.taskmanagement.repository.FonctionRepository;

@Service
public class FonctionService {

    @Autowired
    private FonctionRepository fonctionRepository;

    public FonctionDTO createFonction(FonctionDTO fonctionDTO) {
        Fonction fonction = new Fonction();
        fonction.setNonFonction(fonctionDTO.getNonFonction());
        fonction.setCreateDate(fonctionDTO.getCreateDate());
        fonction.setUpdateDate(fonctionDTO.getUpdateDate());
        Fonction savedFonction = fonctionRepository.save(fonction);
        return toDTO(savedFonction);
    }

    public FonctionDTO updateFonction(Long id, FonctionDTO fonctionDTO) {
        Optional<Fonction> optionalFonction = fonctionRepository.findById(id);
        if (optionalFonction.isPresent()) {
            Fonction fonction = optionalFonction.get();
            fonction.setNonFonction(fonctionDTO.getNonFonction());
            fonction.setCreateDate(fonctionDTO.getCreateDate());
            fonction.setUpdateDate(fonctionDTO.getUpdateDate());
            Fonction updatedFonction = fonctionRepository.save(fonction);
            return toDTO(updatedFonction);
        }
        return null;
    }

    public void deleteFonction(Long id) {
        fonctionRepository.deleteById(id);
    }

    public FonctionDTO getFonctionById(Long id) {
        Optional<Fonction> optionalFonction = fonctionRepository.findById(id);
        return optionalFonction.map(this::toDTO).orElse(null);
    }

    public List<FonctionDTO> getAllFonctions() {
        return fonctionRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private FonctionDTO toDTO(Fonction fonction) {
        FonctionDTO fonctionDTO = new FonctionDTO();
        fonctionDTO.setFonctionId(fonction.getFonctionId());
        fonctionDTO.setNonFonction(fonction.getNonFonction());
        fonctionDTO.setCreateDate(fonction.getCreateDate());
        fonctionDTO.setUpdateDate(fonction.getUpdateDate());
        return fonctionDTO;
    }
}
