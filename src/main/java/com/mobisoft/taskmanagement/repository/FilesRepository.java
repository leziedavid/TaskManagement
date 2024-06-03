package com.mobisoft.taskmanagement.repository;

import com.mobisoft.taskmanagement.entity.Files;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilesRepository extends JpaRepository<Files, Long> {
    
}
