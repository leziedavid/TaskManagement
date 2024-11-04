package com.mobisoft.taskmanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.taskmanagement.dto.AbonnementDTO;
import com.mobisoft.taskmanagement.entity.Abonnement;
import com.mobisoft.taskmanagement.repository.AbonnementRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AbonnementService {

    @Autowired
    private AbonnementRepository abonnementRepository;

    @Autowired
    private UserService userService; // Ajout du service utilisateur

    @Autowired
    private SendEmailServices sendEmailServices; // Utilisation du nom correct


    // public AbonnementDTO createAbonnement(AbonnementDTO abonnementDTO) {
    //     Abonnement abonnement = convertToEntity(abonnementDTO);
    //     abonnement.setCreatedAt(LocalDateTime.now()); // Assigner la date de création
    //     Abonnement savedAbonnement = abonnementRepository.save(abonnement);
    //     return convertToDTO(savedAbonnement);
    // }


    public List<AbonnementDTO> createAbonnement(Long taskId, AbonnementDTO abonnementDTO) {
        List<Abonnement> abonnements = abonnementDTO.getUserIds().stream()
            .map(userId -> {
                Abonnement abonnement = new Abonnement();
                abonnement.setIdTask(taskId);
                abonnement.setUserId(userId);
                abonnement.setCreatedAt(LocalDateTime.now());
    
                // Récupérer l'e-mail de l'utilisateur
                String userEmail = userService.getEmailById(userId);
                abonnement.setEmail(userEmail);

                // Récupérer l'e-mail de l'utilisateur
                String userName = userService.findNameById(userId);
                abonnement.setName(userName);
    
                // Préparer et envoyer l'email
                String senderId = "mobitask@mobisoft.ci";
                String subject = "Vous avez été ajouté en tant que collaborateur";
                String message = "Bonjour,\n\nVous avez été ajouté en tant que collaborateur à une tâche.\n"
                        + "Veuillez vous connecter à Mobitask pour plus d'informations.\n\nCordialement,\nL'équipe Mobisoft.";
    
                // Envoi de l'email à l'utilisateur
                sendEmailServices.sendEmailViaApi(senderId, userId, subject, userEmail, message); // Passez directement userEmail

                return abonnement;
            })
            .collect(Collectors.toList());
    
        // Enregistrer tous les abonnements dans la base de données
        List<Abonnement> savedAbonnements = abonnementRepository.saveAll(abonnements);
    
        // Convertir les abonnements enregistrés en DTO
        return savedAbonnements.stream().map(this::convertToDTO).collect(Collectors.toList());
    }
    
    
    private AbonnementDTO convertToDTO(Abonnement abonnement) {
        AbonnementDTO abonnementDTO = new AbonnementDTO();
        abonnementDTO.setAbonnementId(abonnement.getAbonnementId());
        abonnementDTO.setIdTask(abonnement.getIdTask());
        abonnementDTO.setUserId(abonnement.getUserId()); // Ajouter l'ID de l'utilisateur
        abonnementDTO.setEmail(abonnement.getEmail()); // Ajouter l'email
        abonnementDTO.setEmail(abonnement.getName()); // Ajouter le nom
        abonnementDTO.setCreatedAt(abonnement.getCreatedAt());
        return abonnementDTO;
    }


}

