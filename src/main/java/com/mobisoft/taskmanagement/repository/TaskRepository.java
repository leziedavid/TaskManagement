package com.mobisoft.taskmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mobisoft.taskmanagement.entity.Project;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.entity.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {


    // Récupérer les tâches par projet
    List<Task> findByProject(Project project);

    int countByTaskState(State state);
    List<Task> findByProjectProjectId(Long projectId);

    @Query(value = "SELECT * FROM tasks WHERE project_id = ?1", nativeQuery = true)
    List<Task> findTasksByProjectId(Long projectId);
    
    Optional<Task> findByTaskCode(String taskCode);

    // Méthode pour trouver les tâches assignées à un utilisateur par son ID
    List<Task> findByAssigned_UserId(Long userId);

    // Méthode pour trouver les tâches assignées à un utilisateur par son ID
    List<Task> findByUser_UserId(Long userId);

    // // Méthode pour trouver les tâches associées à un projet donné
    // Set<Task> findByProject_ProjectId(Long projectId);


}