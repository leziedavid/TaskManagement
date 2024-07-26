package com.mobisoft.taskmanagement.repository;

import com.mobisoft.taskmanagement.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // Vous pouvez ajouter des méthodes supplémentaires spécifiques au besoin
}
