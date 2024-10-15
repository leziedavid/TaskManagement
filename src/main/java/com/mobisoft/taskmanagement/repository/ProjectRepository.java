package com.mobisoft.taskmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobisoft.taskmanagement.entity.Project;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.entity.User;
@Repository

public interface ProjectRepository extends JpaRepository<Project, Long> {

  List<Project> findByUser(User user);

    @Query(value = "SELECT u.* FROM users u " +
    "INNER JOIN user_project up ON u.user_id = up.user_id " +
    "WHERE up.project_id = :projectId", nativeQuery = true)
    List<User> findUsersByProjectId(@Param("projectId") Long projectId);
            

      // @SuppressWarnings("null")

      Optional<Project> findById(Long projectId);
      // Méthode pour trouver un projet par projectCodes
      Project findByProjectCodes(String projectCodes);

      // Méthode pour trouver les projets assignés à un utilisateur spécifique
      List<Project> findByUser_UserId(Long userId);

        // Requête native pour trouver les projets assignés à un utilisateur spécifique
        @Query(value = "SELECT p.* FROM projects p " +
        "JOIN user_project up ON p.project_id = up.project_id " +
        "WHERE up.user_id = :userId", nativeQuery = true)
        List<Project> findProjectsByUserId(@Param("userId") Long userId);

      // Méthode pour trouver les projets assignés à un utilisateur spécifique avec pagination
      @Query(value = "SELECT p.* FROM projects p " +
      "JOIN user_project up ON p.project_id = up.project_id " +
      "WHERE up.user_id = :userId", nativeQuery = true)
      Page<Project> findAllProjectsByUserId2(@Param("userId") Long userId, Pageable pageable);
    
      int countByProjectState(State state);


      // @Query("SELECT COUNT(p) FROM Project p WHERE p.user.id = :userId")
      // long countProjectsByUserId(@Param("userId") Long userId);
      @Query("SELECT COUNT(p) FROM Project p " +
      "JOIN UserProject up ON p.projectId = up.project.projectId " +
      "WHERE up.user.userId = :userId")
      long countProjectsByUserId(@Param("userId") Long userId);

  
      // @Query("SELECT COUNT(p) FROM Project p WHERE p.user.id = :userId AND p.projectState = :state")
      // long countProjectsByUserIdAndState(@Param("userId") Long userId, @Param("state") State state);
    @Query("SELECT COUNT(p) FROM Project p "
            + "JOIN UserProject up ON p.projectId = up.project.projectId "
            + "WHERE up.user.userId = :userId AND p.projectState = :state")
    long countProjectsByUserIdAndState(@Param("userId") Long userId, @Param("state") State state);

  
      @Query("SELECT COUNT(p) FROM Project p")
      long countAllProjects();
  
      @Query("SELECT COUNT(p) FROM Project p WHERE p.projectState = :state")
      long countByState(@Param("state") State state);


      // Avoire la liste des projet d'un utilisateur averc ses taches
      
      @Query(value = "SELECT p.* FROM projects p "
      + "JOIN user_project up ON p.project_id = up.project_id "
      + "WHERE up.user_id = :userId "
      + "AND p.project_state != 'TERMINER' "
      + "ORDER BY p.project_created_at DESC",
      nativeQuery = true)
      List<Project> findAllProjectsWithTasksByUserId(@Param("userId") Long userId);

    // ma dernier requette

    // Méthode avec pagination pour récupérer les projets d'un utilisateur
    @Query("SELECT up.project FROM UserProject up WHERE up.user.userId =9")
    Page<Project> findAllProjectsByUserId(@Param("userId") Long userId, Pageable pageable);
    
    @Query(value = "SELECT p FROM Project p "
            + "JOIN UserProject up ON p.projectId = up.project.projectId "
            + "WHERE up.user.userId = :userId "
            + "ORDER BY p.projectCreatedAt DESC",
            countQuery = "SELECT COUNT(p) FROM Project p "
            + "JOIN UserProject up ON p.projectId = up.project.projectId "
            + "WHERE up.user.userId = :userId")
    Page<Project> findPaginatedProjectsByUserId(@Param("userId") Long userId, Pageable pageable);


    // essayon sa
    @Query("SELECT up.project FROM UserProject up WHERE up.user.userId = :userId ORDER BY up.project.projectCreatedAt DESC")
    Page<Project> findProjectsByUserId(@Param("userId") Long userId, Pageable pageable);
    
}
