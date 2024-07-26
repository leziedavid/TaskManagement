package com.mobisoft.taskmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.entity.Project;
@Repository

public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query(value = "SELECT u.* FROM users u " +
    "INNER JOIN user_project up ON u.user_id = up.user_id " +
    "WHERE up.project_id = :projectId", nativeQuery = true)
List<User> findUsersByProjectId(@Param("projectId") Long projectId);
            

    Optional<Project> findById(Long projectId);
     // Méthode pour trouver un projet par projectCodes
    Project findByProjectCodes(String projectCodes);
}
