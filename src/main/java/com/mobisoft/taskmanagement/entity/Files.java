package com.mobisoft.taskmanagement.entity;

import java.time.OffsetDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "files")
@Data
public class Files {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_files;
    private Long id_resource; // L'id de la ressource à laquelle ce fichier est associé
    private String type_files; // Le type de fichier
    private String url_files; // L'URL du fichier sur le serveur
    private String nom_resource; // Le nom de la ressource
    private int status_files = 1; // Le statut du fichier (par défaut 1)
    private OffsetDateTime date_creation;
    private OffsetDateTime date_modification;
}


