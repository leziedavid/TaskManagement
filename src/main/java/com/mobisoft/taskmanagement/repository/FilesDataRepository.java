package com.mobisoft.taskmanagement.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobisoft.taskmanagement.entity.FilesData;

@Repository
public interface FilesDataRepository extends JpaRepository<FilesData, Long> {

    Optional<FilesData> findById(Long id);
    // Méthode pour récupérer les données de stockage par publicId
    Optional<FilesData> findByName(String fileName);
    Optional<FilesData> findByPublicId(String publicId);
}