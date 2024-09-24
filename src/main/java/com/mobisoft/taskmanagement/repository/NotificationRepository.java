package com.mobisoft.taskmanagement.repository;

import com.mobisoft.taskmanagement.entity.Notification;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByStatutLecteur(Integer statutLecteur);
    List<Notification> findByProjectId(Long projectId);

    @Query(value = "SELECT n.notification_id, n.title, n.message, n.created_at, n.statut_lecteur, n.entity_id, n.entity_type, n.project_id, u.user_id, u.username, u.email " +
    "FROM notifications n " +
    "LEFT JOIN user_notifications un ON n.notification_id = un.notification_id " +
    "LEFT JOIN users u ON un.user_id = u.user_id " +
    "WHERE n.notification_id = :id", nativeQuery = true)
    List<Object[]> findNotificationWithUsersById(Long id);


}
