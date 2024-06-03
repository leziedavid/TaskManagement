package com.mobisoft.taskmanagement.service;

import java.io.IOException;
import java.time.OffsetDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mobisoft.taskmanagement.dto.FilesDTO;
import com.mobisoft.taskmanagement.entity.Files;
import com.mobisoft.taskmanagement.repository.FilesRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class FileService {

    @Autowired
    private FilesRepository filesRepository;

    public FilesDTO saveFile(MultipartFile file, Long idResource, String typeFiles, String nomResource) throws IOException {

        Files entity = new Files();
        entity.setId_resource(idResource);
        entity.setType_files(typeFiles);
        entity.setNom_resource(nomResource);
        entity.setStatus_files(1);
        entity.setDate_creation(OffsetDateTime.now());
        entity.setDate_modification(OffsetDateTime.now());

        String fileName = file.getOriginalFilename();
        String fileUrl = "/upload/docs/" + fileName;
        file.transferTo(new java.io.File(fileUrl));
        entity.setUrl_files(fileUrl);

        entity = filesRepository.save(entity);
        FilesDTO dto = convertToDTO(entity);
        return dto;
    }

    public FilesDTO getFileById(Long idFiles) {
        Optional<Files> optionalEntity = filesRepository.findById(idFiles);
        Files entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException("Fichier non trouvé avec l'ID : " + idFiles));
        return convertToDTO(entity);
    }

    public FilesDTO updateFile(Long idFiles, MultipartFile file, String typeFiles, String nomResource) throws IOException {
        Optional<Files> optionalEntity = filesRepository.findById(idFiles);
        Files entity = optionalEntity.orElseThrow(() -> new EntityNotFoundException("Fichier non trouvé avec l'ID : " + idFiles));

        entity.setType_files(typeFiles);
        entity.setNom_resource(nomResource);
        entity.setDate_modification(OffsetDateTime.now());

        if (file != null) {
            String fileName = file.getOriginalFilename();
            String fileUrl = "/upload/docs/" + fileName;
            file.transferTo(new java.io.File(fileUrl));
            entity.setUrl_files(fileUrl);
        }

        entity = filesRepository.save(entity);
        FilesDTO dto = convertToDTO(entity);
        return dto;
    }

    public void deleteFile(Long idFiles) {
        filesRepository.deleteById(idFiles);
    }

    private FilesDTO convertToDTO(Files entity) {
        FilesDTO dto = new FilesDTO();
        dto.setId_files(entity.getId_files());
        dto.setId_resource(entity.getId_resource());
        dto.setType_files(entity.getType_files());
        dto.setNom_resource(entity.getNom_resource());
        dto.setStatus_files(entity.getStatus_files());
        dto.setDate_creation(entity.getDate_creation());
        dto.setDate_modification(entity.getDate_modification());
        dto.setUrl_files(entity.getUrl_files());
        return dto;
    }
}
