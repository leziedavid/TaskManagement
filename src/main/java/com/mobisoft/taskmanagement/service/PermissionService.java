package com.mobisoft.taskmanagement.service;

import com.mobisoft.taskmanagement.dto.PermissionDTO;
import com.mobisoft.taskmanagement.entity.Permission;
import com.mobisoft.taskmanagement.repository.PermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public PermissionDTO createPermission(PermissionDTO permissionDTO) {
        try {
            Permission permission = convertToEntity(permissionDTO);
            Permission savedPermission = permissionRepository.save(permission);
            return convertToDTO(savedPermission);
        } catch (Exception e) {
            throw new EntityNotFoundException("Erreur lors de la création de la permission: " + e.getMessage());
        }
    }

    public PermissionDTO getPermissionById(Long permissionId) {
        Optional<Permission> optionalPermission = permissionRepository.findById(permissionId);
        return optionalPermission.map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Aucune permission trouvée avec l'ID: " + permissionId));
    }

    public List<PermissionDTO> findAllPermissions() {
        try {
            List<Permission> permissions = permissionRepository.findAll();
            return permissions.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de toutes les permissions: " + e.getMessage());
        }
    }

    public PermissionDTO updatePermission(Long permissionId, PermissionDTO permissionDTO) {
        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new EntityNotFoundException("La permission avec l'ID spécifié n'existe pas"));
        updatePermissionFromDTO(permission, permissionDTO);
        Permission updatedPermission = permissionRepository.save(permission);
        return convertToDTO(updatedPermission);
    }

    public boolean deletePermission(Long permissionId) {
        if (!permissionRepository.existsById(permissionId)) {
            throw new EntityNotFoundException("La permission avec l'ID spécifié n'existe pas");
        }
        permissionRepository.deleteById(permissionId);
        return true;
    }

    private PermissionDTO convertToDTO(Permission permission) {
        PermissionDTO permissionDTO = new PermissionDTO();
        // permissionDTO.setPermissionId(permission.getPermissionId());
        permissionDTO.setPermissionName(permission.getPermissionName());
        return permissionDTO;
    }

    private Permission convertToEntity(PermissionDTO permissionDTO) {
        Permission permission = new Permission();
        permission.setPermissionName(permissionDTO.getPermissionName());
        return permission;
    }

    private void updatePermissionFromDTO(Permission permission, PermissionDTO permissionDTO) {
        permission.setPermissionName(permissionDTO.getPermissionName());
    }
}
