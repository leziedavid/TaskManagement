package com.mobisoft.taskmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobisoft.taskmanagement.entity.Leave;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {

    // Méthode pour trouver des congés par l'ID de l'utilisateur
    List<Leave> findByUser_UserId(Long userId);

    // Méthode pour trouver un congé par son ID
    Optional<Leave> findById(Long leaveId);

    // Vous pouvez ajouter d'autres méthodes spécifiques si nécessaire
}
