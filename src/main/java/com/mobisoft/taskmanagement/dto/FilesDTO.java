package com.mobisoft.taskmanagement.dto;
import java.time.OffsetDateTime;
// import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class FilesDTO {
    private Long id_files;
    private Long id_resource;
    private String type_files;
    private String url_files;
    private String nom_resource;
    private int status_files = 1;
    private OffsetDateTime date_creation;
    private OffsetDateTime date_modification;

}
