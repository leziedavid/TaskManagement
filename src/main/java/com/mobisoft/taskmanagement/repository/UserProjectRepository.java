package com.mobisoft.taskmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.entity.UserProject;


@Repository
public interface UserProjectRepository extends JpaRepository<UserProject, Long> {

    // Optional<UserProject> findByProjectIdAndUserId(String projectId, Long userId);
    @Query(value = "SELECT * FROM user_project up WHERE up.project_id = :projectId AND up.user_id = :userId", nativeQuery = true)
    Optional<UserProject> findByProjectIdAndUserId(Long projectId, Long userId);

    List<UserProject> findByProjectId(Long projectId);

    @Query("SELECT up.user FROM UserProject up WHERE up.project.projectId = :projectId")
    List<User> findUsersByProjectId(Long projectId);
    
    List<UserProject> findByProjectIdAndLeader(Long projectId, boolean leader);
    // Optional<UserProject> findByProjectIdAndUserId(Long projectId, Long userId);


}