package com.mobisoft.taskmanagement.controller;

import com.mobisoft.taskmanagement.dto.PermissionDTO;
import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @PostMapping
    public ResponseEntity<BaseResponse<PermissionDTO>> createPermission(@Validated @RequestBody PermissionDTO permissionDTO) {
        PermissionDTO createdPermission = permissionService.createPermission(permissionDTO);
        BaseResponse<PermissionDTO> response = new BaseResponse<>(201, "Permission créée avec succès", createdPermission);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<PermissionDTO>> getPermissionById(@PathVariable Long id) {
        PermissionDTO permission = permissionService.getPermissionById(id);
        BaseResponse<PermissionDTO> response = new BaseResponse<>(200, "Détails de la permission", permission);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllPermissions")
    public ResponseEntity<BaseResponse<List<PermissionDTO>>> getAllPermissions() {
        List<PermissionDTO> permissions = permissionService.findAllPermissions();
        BaseResponse<List<PermissionDTO>> response = new BaseResponse<>(200, "Liste des permissions", permissions);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<PermissionDTO>> updatePermission(@PathVariable Long id,@Validated @RequestBody PermissionDTO permissionDTO) {
        PermissionDTO updatedPermission = permissionService.updatePermission(id, permissionDTO);
        BaseResponse<PermissionDTO> response = new BaseResponse<>(200, "Permission mise à jour avec succès", updatedPermission);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Permission supprimée avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
