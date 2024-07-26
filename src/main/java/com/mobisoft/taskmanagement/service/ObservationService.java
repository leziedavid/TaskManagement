package com.mobisoft.taskmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.mobisoft.taskmanagement.dto.ObservationDTO;
import com.mobisoft.taskmanagement.entity.Observation;
import com.mobisoft.taskmanagement.entity.Task;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.repository.ObservationRepository;
import com.mobisoft.taskmanagement.repository.TaskRepository;
import com.mobisoft.taskmanagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ObservationService {

    @Autowired
    private ObservationRepository observationRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public ObservationDTO createObservation(ObservationDTO observationDTO, List<MultipartFile> files) {
        try {
            Observation observation = convertToEntity(observationDTO);
            Observation savedObservation = observationRepository.save(observation);
                // Vérifie si la liste de fichiers n'est pas nulle et n'est pas vide
                if (files != null && !files.isEmpty()) {
                    for (MultipartFile file : files) {
                        String publicId = fileStorageService.uploadImage(file);
                        observationRepository.insertObservationFileRelation(savedObservation.getObservationId(), publicId);
                    }
                }
            return convertToDTO(savedObservation);
        } catch (Exception e) {
            throw new EntityNotFoundException("Erreur lors de la création de l'observation: " + e.getMessage());
        }

    }
    
    public ObservationDTO updateObservation(Long observationId, ObservationDTO observationDTO, List<MultipartFile> files) {
        try {
            // Recherche de l'observation à mettre à jour
            Observation observationToUpdate = observationRepository.findById(observationId).orElseThrow(() -> new EntityNotFoundException("Aucune observation trouvée avec l'ID: " + observationId));
            
            // Mise à jour des attributs de l'observation à partir du DTO
            observationToUpdate.setLibelle(observationDTO.getLibelle());
            observationToUpdate.setDescription(observationDTO.getDescription());
            if (observationDTO.getTaskId() != null) {
                Task task = taskRepository.findById(observationDTO.getTaskId()).orElseThrow(() -> new EntityNotFoundException("La tâche avec l'ID spécifié n'existe pas"));
                observationToUpdate.setTask(task);
            }
            if (observationDTO.getUserId() != null) {
                User user = userRepository.findById(observationDTO.getUserId()).orElseThrow(() -> new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas"));
                observationToUpdate.setUser(user);
            }
            // Sauvegarde de l'observation mise à jour
            Observation updatedObservation = observationRepository.save(observationToUpdate);
            return convertToDTO(updatedObservation);
            
        } catch (Exception e) {
            throw new EntityNotFoundException("Erreur lors de la mise à jour de l'observation: " + e.getMessage());
        }
    }

    public ObservationDTO getObservationById(Long observationId) {
        Optional<Observation> optionalObservation = observationRepository.findById(observationId);
        return optionalObservation.map(this::convertToDTO).orElseThrow(() -> new EntityNotFoundException("Aucune observation trouvée avec l'ID: " + observationId));
    }

    public List<ObservationDTO> findAllObservations() {
        List<Observation> observations = observationRepository.findAll();
        return observations.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }


    public boolean deleteObservation(Long observationId) {
        if (!observationRepository.existsById(observationId)) {
            throw new EntityNotFoundException("L'observation avec l'ID spécifié n'existe pas");
        }
        observationRepository.deleteById(observationId);
        return true;
    }

    private ObservationDTO convertToDTO(Observation observation) {
        ObservationDTO observationDTO = new ObservationDTO();
        // observationDTO.setObservationId(observation.getObservationId());
        observationDTO.setLibelle(observation.getLibelle());
        observationDTO.setDescription(observation.getDescription());
        observationDTO.setTaskId(observation.getTask().getTaskId());
        observationDTO.setUserId(observation.getUser().getUserId());
        return observationDTO;
    }

    private Observation convertToEntity(ObservationDTO observationDTO) {
        Observation observation = new Observation();
        observation.setLibelle(observationDTO.getLibelle());
        observation.setDescription(observationDTO.getDescription());
        if (observationDTO.getTaskId() != null) {
            Task task = taskRepository.findById(observationDTO.getTaskId())
            .orElseThrow(() -> new EntityNotFoundException("La tâche avec l'ID spécifié n'existe pas"));
            observation.setTask(task);
        }
        if (observationDTO.getUserId() != null) {
            User user = userRepository.findById(observationDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas"));
            observation.setUser(user);
        }
        return observation;
    }


}
