package com.mobisoft.taskmanagement.service;

import com.mobisoft.taskmanagement.dto.ObservationDTO;
import com.mobisoft.taskmanagement.entity.Observation;
import com.mobisoft.taskmanagement.entity.Task;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.repository.ObservationRepository;
import com.mobisoft.taskmanagement.repository.TaskRepository;
import com.mobisoft.taskmanagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ObservationService {

    @Autowired
    private ObservationRepository observationRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public ObservationDTO createObservation(ObservationDTO observationDTO) {
        Observation observation = convertToEntity(observationDTO);
        Observation savedObservation = observationRepository.save(observation);
        return convertToDTO(savedObservation);
    }

    public ObservationDTO getObservationById(Long observationId) {
        Optional<Observation> optionalObservation = observationRepository.findById(observationId);
        return optionalObservation.map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Aucune observation trouvée avec l'ID: " + observationId));
    }

    public List<ObservationDTO> findAllObservations() {
        List<Observation> observations = observationRepository.findAll();
        return observations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ObservationDTO updateObservation(Long observationId, ObservationDTO observationDTO) {
        Observation observation = observationRepository.findById(observationId)
                .orElseThrow(() -> new EntityNotFoundException("L'observation avec l'ID spécifié n'existe pas"));
        updateObservationFromDTO(observation, observationDTO);
        Observation updatedObservation = observationRepository.save(observation);
        return convertToDTO(updatedObservation);
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
        observationDTO.setObservationId(observation.getObservationId());
        observationDTO.setLibelle(observation.getLibelle());
        observationDTO.setDescription(observation.getDescription());
        observationDTO.setFile(observation.getFile());
        observationDTO.setTaskId(observation.getTask().getTaskId());
        observationDTO.setUserId(observation.getUser().getUserId());
        return observationDTO;
    }

    private Observation convertToEntity(ObservationDTO observationDTO) {
        Observation observation = new Observation();
        observation.setLibelle(observationDTO.getLibelle());
        observation.setDescription(observationDTO.getDescription());
        observation.setFile(observationDTO.getFile());

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

    private void updateObservationFromDTO(Observation observation, ObservationDTO observationDTO) {
        observation.setLibelle(observationDTO.getLibelle());
        observation.setDescription(observationDTO.getDescription());
        observation.setFile(observationDTO.getFile());

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
    }
}
