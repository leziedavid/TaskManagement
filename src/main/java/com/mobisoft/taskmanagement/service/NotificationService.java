package com.mobisoft.taskmanagement.service;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.taskmanagement.dto.NotificationDTO;
import com.mobisoft.taskmanagement.dto.UserDTO;
import com.mobisoft.taskmanagement.entity.Notification;
import com.mobisoft.taskmanagement.entity.Project;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.repository.NotificationRepository;
import com.mobisoft.taskmanagement.repository.ProjectRepository;
import com.mobisoft.taskmanagement.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    public NotificationDTO createNotification(NotificationDTO notificationDTO) {

        Notification notification = convertToEntity(notificationDTO);
        Notification savedNotification = notificationRepository.save(notification);
        return convertToDTO(savedNotification);
    }

    public NotificationDTO createNotificationForAction(String title, String message, Long entityId, String entityType, Set<Long> userIds,Long projectId,Long addBy) {

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setTitle(title);
        notificationDTO.setMessage(message);
        notificationDTO.setEntityId(entityId);
        notificationDTO.setEntityType(entityType);
        notificationDTO.setCreatedAt(OffsetDateTime.now().toString());
        notificationDTO.setUserIds(userIds);
        notificationDTO.setProjectId(projectId);
        notificationDTO.setAddBy(addBy);
        
        return createNotification(notificationDTO);
    }


    public List<NotificationDTO> getAllNotifications() {
        List<Notification> notifications = notificationRepository.findAll();
        return notifications.stream().map(this::convertToDTO) // Convertir chaque Notification en NotificationDTO
        .collect(Collectors.toList());
    }

    public NotificationDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Notification non trouvée avec l'ID: " + id));
        return convertToDTO(notification); // Convertir Notification en NotificationDTO
    }

    public List<NotificationDTO> getUnreadNotifications() {
        List<Notification> notifications = notificationRepository.findByStatutLecteur(0);
        return notifications.stream().map(this::convertToDTO) // Convertir chaque Notification en NotificationDTO
        .collect(Collectors.toList());
    }
    

    public List<NotificationDTO> getNotificationsByProjectId(String projectId) {

                Project project = projectRepository.findByProjectCodes(projectId);

        if (project == null) {
            throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + projectId);
        }

        Long id = project.getProjectId();
        List<Notification> notifications = notificationRepository.findByProjectId(id);
        return notifications.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Dans NotificationService.java
    private NotificationDTO convertToDTO(Notification notification) {

        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setNotificationId(notification.getNotificationId());
        notificationDTO.setTitle(notification.getTitle());
        notificationDTO.setMessage(notification.getMessage());
        notificationDTO.setCreatedAt(notification.getCreatedAt().toString());
        notificationDTO.setEntityId(notification.getEntityId()); // Utilisation correcte
        notificationDTO.setEntityType(notification.getEntityType()); // Utilisation correcte

        notificationDTO.setUserIds(notification.getUsers().stream().map(User::getUserId).collect(Collectors.toSet()));

            // Convertir les utilisateurs associés en UserDTO
        // Set<UserDTO> userDTOs = notification.getUsers().stream().map(this::convertToUserDTO).collect(Collectors.toSet());
        // notificationDTO.setUserlistes(userDTOs);

        // Charger l'utilisateur qui a ajouté la notification
        Long addByUserId = notification.getUser() != null ? notification.getUser().getUserId() : null;
        
        if (addByUserId != null) {
            User addByUser = userRepository.findById(addByUserId).orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'ID: " + addByUserId));
            UserDTO addByUserDTO = convertToUserDTO(addByUser);
            notificationDTO.setUserAddBBy(Set.of(addByUserDTO)); // Utiliser Set.of pour créer un ensemble avec un seul élément
        }

        notificationDTO.setStatutLecteur(notification.getStatutLecteur());

        return notificationDTO;

    }

    private UserDTO convertToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setLastname(user.getLastname());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhone(user.getPhone());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setFonction(user.getFonction());
        userDTO.setToken(user.getToken());
        userDTO.setIsValides(user.getIsValides());
        userDTO.setRole(user.getRole());
        userDTO.setProfil(user.getProfil());
        return userDTO;
    }

    private Notification convertToEntity(NotificationDTO notificationDTO) {
        Notification notification = new Notification();
        notification.setTitle(notificationDTO.getTitle());
        notification.setMessage(notificationDTO.getMessage());
        notification.setCreatedAt(OffsetDateTime.parse(notificationDTO.getCreatedAt()));
        notification.setEntityId(notificationDTO.getEntityId());
        notification.setEntityType(notificationDTO.getEntityType());
        notification.setStatutLecteur(notificationDTO.getStatutLecteur());
        notification.setProjectId(notificationDTO.getProjectId());
        // Récupérer l'utilisateur à partir de UserDTO
        Long addById = notificationDTO.getAddBy(); // Assurez-vous que getUserId() retourne Long
        User user = userRepository.findById(addById).orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'ID: " + addById));

        notification.setUser(user);
        Set<User> users = new HashSet<>();
        if (notificationDTO.getUserIds() != null) {
            for (Long userId : notificationDTO.getUserIds()) {
                User u = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));
                users.add(u);
            }
        }
        notification.setUsers(users);
        
        return notification;
    }
    
    


    // private Notification convertToEntity(NotificationDTO notificationDTO) {
    //     Notification notification = new Notification();
    //     notification.setTitle(notificationDTO.getTitle());
    //     notification.setMessage(notificationDTO.getMessage());
    //     notification.setCreatedAt(OffsetDateTime.parse(notificationDTO.getCreatedAt()));
    //     notification.setEntityId(notificationDTO.getEntityId());
    //     notification.setEntityType(notificationDTO.getEntityType());
    //     notification.setStatutLecteur(0);
    //     notification.setProjectId(notificationDTO.getProjectId());
    
    //     // Récupérer l'utilisateur à partir de l'ID
    //     User user = userRepository.findById(notificationDTO.getAddBy())
    //         .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'ID: " + notificationDTO.getAddBy()));
    //     notification.setUser(user);
    
    //     Set<User> users = new HashSet<>();
    //     if (notificationDTO.getUserIds() != null) {
    //         for (Long userId : notificationDTO.getUserIds()) {
    //             User u = userRepository.findById(userId)
    //                 .orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé avec l'ID: " + userId));
    //             users.add(u);
    //         }
    //     }
    //     notification.setUsers(users);
    
    //     return notification;
    // }
    
}
