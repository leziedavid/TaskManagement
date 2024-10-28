package com.mobisoft.taskmanagement.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mobisoft.taskmanagement.entity.FilesData;
import com.mobisoft.taskmanagement.entity.Observation;
import com.mobisoft.taskmanagement.entity.Project;
import com.mobisoft.taskmanagement.repository.FilesDataRepository;
import com.mobisoft.taskmanagement.repository.ObservationRepository;
import com.mobisoft.taskmanagement.repository.ProjectRepository;

import jakarta.persistence.EntityNotFoundException;



@Service
public class FileStorageService {

    private final String uploadDir = "C:\\Users\\MOBISOFT_012\\Desktop\\TDLLEZIE\\TaskBakend\\Documents";
    private final String uploadProfilDir = "C:\\Users\\MOBISOFT_012\\Desktop\\TDLLEZIE\\TaskBakend\\Documents";

    @Autowired
    private FilesDataRepository filesDataRepository;


    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ObservationRepository observationRepository;

    public String uploadFileWithTitle(MultipartFile file, String title) throws IOException {
        
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Assurer que le répertoire d'upload existe, sinon le créer
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un UUID aléatoire pour le nom du fichier
        String uuid = UUID.randomUUID().toString();

        // Obtenir l'extension du fichier d'origine
        String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
        String fileExtension = FilenameUtils.getExtension(fileName);

        // Nouveau nom de fichier avec UUID + extension
        String newFileName = uuid + "." + fileExtension;
        // Chemin complet du fichier à enregistrer
        Path filePath = uploadPath.resolve(newFileName);
        // Enregistrer le fichier dans le répertoire
        Files.copy(file.getInputStream(), filePath);

        // Créer une instance FilesData pour sauvegarder en base de données
        FilesData filesData = FilesData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .size(file.getSize())
                .filePath(filePath.toString())
                .title(title)
                .dateCreation(OffsetDateTime.now())
                .dateModification(OffsetDateTime.now())
                .build();

        // Sauvegarder dans la base de données
        filesData = filesDataRepository.save(filesData);

        // Générer le publicId et mettre à jour dans la base de données
        String publicId = generatePublicId(filesData.getId());
        filesData.setPublicId(publicId);
        filesDataRepository.save(filesData);

        // Retourner l'ID du fichier sauvegardé
        return filesData.getId().toString();
    }

    public String uploadImage(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Assurer que le répertoire d'upload existe, sinon le créer
        Path uploadPath = Paths.get(uploadProfilDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un UUID aléatoire pour le nom du fichier
        String uuid = UUID.randomUUID().toString();

        // Obtenir l'extension du fichier d'origine
        String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
        String fileExtension = FilenameUtils.getExtension(fileName);

        // Nouveau nom de fichier avec UUID + extension
        String newFileName = uuid + "." + fileExtension;
        // Chemin complet du fichier à enregistrer
        Path filePath = uploadPath.resolve(newFileName);
        // Enregistrer le fichier dans le répertoire
        Files.copy(file.getInputStream(), filePath);

        // Créer une instance FilesData pour sauvegarder en base de données
        FilesData filesData = FilesData.builder()
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .size(file.getSize())
                .filePath(filePath.toString())
                .dateCreation(OffsetDateTime.now())
                .dateModification(OffsetDateTime.now())
                .build();

        // Sauvegarder dans la base de données
        filesData = filesDataRepository.save(filesData);

        // Générer le publicId et mettre à jour dans la base de données
        String publicId = generatePublicId(filesData.getId());
        filesData.setPublicId(publicId);
        filesDataRepository.save(filesData);

        return filesData.getPublicId();
    }

    private  String generatePublicId(Long id) {
        String idString = String.valueOf(id);
        byte[] encodedBytes = Base64.getEncoder().encode(idString.getBytes());
        return new String(encodedBytes);
    }
    
    public String downloadImage(String codes) {
        Optional<FilesData> dbFilesData = filesDataRepository.findByPublicId(codes);
        if (dbFilesData.isPresent()) {
            return dbFilesData.get().getFilePath();
        } else {
            return null;
        }
    }

    public InputStreamResource downloadFile(String publicId, HttpHeaders headers) throws FileNotFoundException {
        FilesData dbFilesData = filesDataRepository.findByPublicId(publicId)
        .orElseThrow(() -> new EntityNotFoundException("Erreur lors de la lecture du fichier: " + publicId));
        String filePath = dbFilesData.getFilePath();
        File file = new File(filePath);
        headers.add("content-disposition", "inline;filename="+file.getName());
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Content-Type", dbFilesData.getType());
        return new InputStreamResource(new FileInputStream(file));
    }

    public void assignFilesToProject(Long projectId, Long userId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException("Le projet avec l'ID spécifié n'existe pas"));
        FilesData filesData = filesDataRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Le fichier avec l'ID spécifié n'existe pas"));
        project.getFilesData().add(filesData);
        projectRepository.save(project);
    }

    public void assignFilesToObservation(Long observationId, Long userId) {
        Observation observation = observationRepository.findById(observationId).orElseThrow(() -> new EntityNotFoundException("L'observation avec l'ID spécifié n'existe pas"));
        FilesData filesData = filesDataRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Le fichier avec l'ID spécifié n'existe pas"));
        observation.getFilesData().add(filesData);
        observationRepository.save(observation);
    }

}
