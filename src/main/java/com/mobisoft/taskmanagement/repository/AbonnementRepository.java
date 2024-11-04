package com.mobisoft.taskmanagement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.mobisoft.taskmanagement.entity.Abonnement;

@Repository
public interface AbonnementRepository extends JpaRepository<Abonnement, Long> {
    // Ici, vous pouvez ajouter des méthodes personnalisées si nécessaire

    List<Abonnement> findByIdTask(Long taskId); // Méthode pour trouver par idTask
    
}

