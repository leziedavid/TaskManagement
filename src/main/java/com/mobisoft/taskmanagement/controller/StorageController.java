package com.mobisoft.taskmanagement.controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.service.FileStorageService;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.core.MediaType;

@RestController
@RequestMapping("/api/v1/storage")
public class StorageController {

    @Autowired
    private FileStorageService fileService;

    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @PostMapping
    public ResponseEntity<BaseResponse<List<String>>> uploadImage(@RequestParam("files") List<MultipartFile> files) {

        List<String> uploadResults = files.stream()
                .map(file -> {
                    try {
                        return fileService.uploadImage(file);
                    } catch (IOException e) {
                        return "Erreur lors du téléchargement du fichier : " + file.getOriginalFilename();
                    }
                }).collect(Collectors.toList());

        BaseResponse<List<String>> response = new BaseResponse<>(HttpStatus.OK.value(), "Téléchargement terminé", uploadResults);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{publicId}")
    public ResponseEntity<Object> downloadFile(@PathVariable String publicId) throws FileNotFoundException {
        System.out.println("bonjour");
        HttpHeaders headers = new HttpHeaders();
        InputStreamResource fileResource = fileService.downloadFile(publicId, headers);
        return ResponseEntity.ok()
        .headers(headers)
        .body(fileResource);
        
    }


}
