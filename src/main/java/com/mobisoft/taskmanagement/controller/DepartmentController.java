package com.mobisoft.taskmanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.dto.DepartmentDTO;
import com.mobisoft.taskmanagement.service.DepartmentService;

@RestController
@Validated
@RequestMapping("/api/v1/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    public ResponseEntity<BaseResponse<DepartmentDTO>> createDepartment(@Validated @RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO createdDepartment = departmentService.createDepartment(departmentDTO);
        BaseResponse<DepartmentDTO> response = new BaseResponse<>(201, "Département créé avec succès", createdDepartment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<DepartmentDTO>> getDepartmentById(@PathVariable Long id) {
        DepartmentDTO department = departmentService.getDepartmentById(id);
        BaseResponse<DepartmentDTO> response = new BaseResponse<>(200, "Détails du département", department);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/getAllDepartments")
    public ResponseEntity<BaseResponse<List<DepartmentDTO>>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.findAllDepartments();
        BaseResponse<List<DepartmentDTO>> response = new BaseResponse<>(200, "Liste des départements", departments);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<DepartmentDTO>> updateDepartment(@PathVariable Long id, @Validated @RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO updatedDepartment = departmentService.updateDepartment(id, departmentDTO);
        BaseResponse<DepartmentDTO> response = new BaseResponse<>(200, "Département mis à jour avec succès", updatedDepartment);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/{departmentId}/users")
    public ResponseEntity<BaseResponse<List<?>>> getUsersByDepartmentId(@PathVariable Long departmentId) {
        List<?> users = departmentService.getUsersByDepartmentId(departmentId);
        BaseResponse<List<?>> response = new BaseResponse<>(200, "Liste des utilisateurs du département", users);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Département supprimé avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/{departmentId}/assignUser/{userId}")
    public ResponseEntity<BaseResponse<Void>> assignUserToDepartment(@PathVariable Long departmentId, @PathVariable Long userId) {
        departmentService.assignUserToDepartment(departmentId, userId);
        BaseResponse<Void> response = new BaseResponse<>(200, "Utilisateur assigné au département avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
