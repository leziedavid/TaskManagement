package com.mobisoft.taskmanagement.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobisoft.taskmanagement.dto.BaseResponse;
import com.mobisoft.taskmanagement.dto.DepartmentDTO;
import com.mobisoft.taskmanagement.dto.DepartmentsResponse;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.service.DepartmentService;

@RestController
@Validated
@RequestMapping("/api/v1")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;



    @PostMapping("/departments/Adddepartments")
    public ResponseEntity<BaseResponse<DepartmentDTO>> Adddepartments(@Validated @RequestBody DepartmentDTO departmentDTO) {
        DepartmentDTO createdDepartment = departmentService.Adddepartments(departmentDTO);
        BaseResponse<DepartmentDTO> response = new BaseResponse<>(201, "Département créé avec succès", createdDepartment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/departments/getDepartmentById/{id}")
    public ResponseEntity<BaseResponse<DepartmentDTO>> getDepartmentById(@PathVariable Long id) {
        DepartmentDTO department = departmentService.getDepartmentById(id);
        BaseResponse<DepartmentDTO> response = new BaseResponse<>(200, "Détails du département", department);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/departments/getAllDepartments2")
    public ResponseEntity<BaseResponse<DepartmentsResponse>> getAllDepartments2(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "departmentId") String sortBy) {
        
        if (page < 0 || size <= 0) {
            return new ResponseEntity<>(new BaseResponse<>(400, "Invalid page or size", null), HttpStatus.BAD_REQUEST);
        }
        
        // Vérifiez si sortBy est valide
        List<String> validSortFields = Arrays.asList("departmentId", "departmentName"); // Ajoutez d'autres champs si nécessaire
        if (!validSortFields.contains(sortBy)) {
            return new ResponseEntity<>(new BaseResponse<>(400, "Invalid sortBy field", null), HttpStatus.BAD_REQUEST);
        }
    
        DepartmentsResponse departmentsResponse = departmentService.findAllDepartments2(page, size, sortBy);
        BaseResponse<DepartmentsResponse> response = new BaseResponse<>(200, "Liste des départements", departmentsResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    


    @GetMapping("/departments/getAllDepartments")
    public ResponseEntity<BaseResponse<List<DepartmentDTO>>> getAllDepartments() {
        List<DepartmentDTO> departments = departmentService.findAllDepartments();
        BaseResponse<List<DepartmentDTO>> response = new BaseResponse<>(200, "Liste des départements", departments);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // @PutMapping("departments/updateDepartment/{id}")
    // public ResponseEntity<BaseResponse<DepartmentDTO>> updateDepartment(@PathVariable Long id, @Validated @RequestBody DepartmentDTO departmentDTO) {
    //     DepartmentDTO updatedDepartment = departmentService.updateDepartment(id, departmentDTO);
    //     BaseResponse<DepartmentDTO> response = new BaseResponse<>(200, "Département mis à jour avec succès", updatedDepartment);
    //     return new ResponseEntity<>(response, HttpStatus.OK);
    // }


    // @GetMapping("/{departmentId}/users")
    // public ResponseEntity<BaseResponse<List<?>>> getUsersByDepartmentId(@PathVariable Long departmentId) {
    //     List<?> users = departmentService.getUsersByDepartmentId(departmentId);
    //     BaseResponse<List<?>> response = new BaseResponse<>(200, "Liste des utilisateurs du département", users);
    //     return ResponseEntity.ok(response);
    // }


    @GetMapping("departments/{departmentId}/users")
    public ResponseEntity<BaseResponse<List<User>>> getUsersByDepartmentId(@PathVariable Long departmentId) {
        List<User> users = departmentService.getUsersByDepartmentId(departmentId);
        BaseResponse<List<User>> response = new BaseResponse<>(200, "Liste des utilisateurs du département", users);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/departments/users")
    public ResponseEntity<BaseResponse<List<User>>> getUsersByDepartmentIds(
            @RequestParam("ids") String ids) {
        // Convertir la chaîne de caractères "2,3,4,5" en liste de Longs
        List<Long> departmentIds = Stream.of(ids.split(",")).map(Long::parseLong).collect(Collectors.toList());

        List<User> users = departmentService.getUsersByDepartmentIds(departmentIds);
        BaseResponse<List<User>> response = new BaseResponse<>(200, "Liste des utilisateurs des départements", users);
        return ResponseEntity.ok(response);
    }


    @DeleteMapping("departments/deleteDepartment/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        BaseResponse<Void> response = new BaseResponse<>(200, "Département supprimé avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("departments/{departmentId}/assignUser/{userId}")
    public ResponseEntity<BaseResponse<Void>> assignUserToDepartment(@PathVariable Long departmentId, @PathVariable Long userId) {
        departmentService.assignUserToDepartment(departmentId, userId);
        BaseResponse<Void> response = new BaseResponse<>(200, "Utilisateur assigné au département avec succès", null);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
