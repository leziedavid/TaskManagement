package com.mobisoft.taskmanagement.controller;


import java.io.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
// import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
// import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.service.FileStorageService;


@RestController
public class FileStorageServiceController {

    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/api/v1/{publicId}")
    
    public ResponseEntity<Object> downloadFile(@PathVariable String publicId) throws FileNotFoundException {
        System.out.println("bonjour");
        HttpHeaders headers = new HttpHeaders();
        InputStreamResource fileResource = fileStorageService.downloadFile(publicId, headers);
        return ResponseEntity.ok()
        .headers(headers)
        .body(fileResource);
        
    }

}
