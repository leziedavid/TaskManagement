package com.mobisoft.taskmanagement.repository;



import com.mobisoft.taskmanagement.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.mobisoft.taskmanagement.entity.Department;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT u FROM User u JOIN u.departments d WHERE d.departmentId = :departmentId")
    List<User> findUsersByDepartmentId(@Param("departmentId") Long departmentId);

    @Query("SELECT d FROM Department d JOIN d.users u WHERE u.id = :userId")
    List<Department> findByUsersId(@Param("userId") Long userId);


    @Query("SELECT d FROM Department d JOIN d.users u WHERE u.id = :userId")
    List<Department> findDepartmentsByUserId(@Param("userId") Long userId);

    @Query("SELECT d FROM Department d JOIN d.users u WHERE u.userId = :userId")
    List<Department> findDepartmentsOfUser(@Param("userId") Long userId);


}


