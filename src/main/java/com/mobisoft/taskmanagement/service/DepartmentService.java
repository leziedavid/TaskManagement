package com.mobisoft.taskmanagement.service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobisoft.taskmanagement.dto.DepartmentDTO;
import com.mobisoft.taskmanagement.entity.Department;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.repository.DepartmentRepository;
import com.mobisoft.taskmanagement.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DepartmentService {

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    public DepartmentDTO createDepartment(DepartmentDTO departmentDTO) {
        Department department = convertToEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        return convertToDTO(savedDepartment);
    }
    
    public List<DepartmentDTO> findAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public DepartmentDTO getDepartmentById(Long id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
            if (optionalDepartment.isPresent()) {
                return convertToDTO(optionalDepartment.get());
            } else {
                throw new EntityNotFoundException("Aucun département trouvé avec l'ID: " + id);
            }
    }


    public List<?> getUsersByDepartmentId(Long departmentId) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new EntityNotFoundException("Le département avec l'ID spécifié n'existe pas"));
        // System.out.println(department.getUsers().toString());
        return objectMapper.convertValue(department.getUsers(), List.class);
    }

    public DepartmentDTO updateDepartment(Long departmentId, DepartmentDTO departmentDTO) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new EntityNotFoundException("Le département avec l'ID spécifié n'existe pas"));
        updateDepartmentFromDTO(department, departmentDTO);
        Department updatedDepartment = departmentRepository.save(department);
        return convertToDTO(updatedDepartment);
    }


    public boolean deleteDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new EntityNotFoundException("Le département avec l'ID spécifié n'existe pas");
        }
        departmentRepository.deleteById(departmentId);
        return true;
    }

    /**
     * Associe un utilisateur à un département lors de l'inscription.
     */
    public void assignUserToDepartment(Long departmentId, Long userId) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new EntityNotFoundException("Le département avec l'ID spécifié n'existe pas"));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas"));
        department.getUsers().add(user);
        departmentRepository.save(department);
    }

    private DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentId(department.getDepartmentId());
        departmentDTO.setDepartmentName(department.getDepartmentName());
        departmentDTO.setDepartmentCreatedAt(department.getDepartmentCreatedAt());
        departmentDTO.setDepartmentUpdatedAt(department.getDepartmentUpdatedAt());
        // departmentDTO.setUsers(department.getUsers());
        // departmentDTO.setProjects(department.getProjects());
        return departmentDTO;
    }


    private Department convertToEntity(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setDepartmentName(departmentDTO.getDepartmentName());
        department.setDepartmentCreatedAt(OffsetDateTime.now());
        department.setDepartmentUpdatedAt(OffsetDateTime.now());
        return department;
    }


    private void updateDepartmentFromDTO(Department department, DepartmentDTO departmentDTO) {
        department.setDepartmentName(departmentDTO.getDepartmentName());
        department.setDepartmentUpdatedAt(OffsetDateTime.now());
    }

}
