package com.mobisoft.taskmanagement.dto;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class FilesDTO {
    private Long fileId;
    private String originalName;
    private String mimetype;
    private Long size;
    private String publicId;
    private String title;
    private OffsetDateTime dateCreation;
    private OffsetDateTime dateModification;

    public boolean isEmpty() {
        return this.fileId == null && this.originalName == null && this.mimetype == null && this.size == null && this.publicId == null && this.dateCreation == null && this.dateModification == null;
    }
    
}
