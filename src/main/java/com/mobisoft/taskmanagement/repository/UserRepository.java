package com.mobisoft.taskmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mobisoft.taskmanagement.entity.Gender;
import com.mobisoft.taskmanagement.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    long countByGenre(Gender genre);  // Assurez-vous que la méthode est correctement définie

    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findByToken(String token); // Ajoutez cette méthode
    
    @Query("SELECT u FROM User u LEFT JOIN FETCH u.departments")
    List<User> findAllUsersWithDepartments();

    List<User> findByDepartmentsDepartmentId(Long departmentId);

    @Query(value = "SELECT u.* FROM users u " +"JOIN department_users du ON u.user_id = du.user_id " + "WHERE du.department_id = :departmentId", nativeQuery = true)
    List<User> findUsersByDepartmentId(@Param("departmentId") Long departmentId);



}
