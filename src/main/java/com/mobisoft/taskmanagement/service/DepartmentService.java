package com.mobisoft.taskmanagement.service;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.taskmanagement.dto.DepartmentDTO;
import com.mobisoft.taskmanagement.dto.DepartmentsResponse;
import com.mobisoft.taskmanagement.entity.Department;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.pagination.Page;
import com.mobisoft.taskmanagement.pagination.Paginator;
import com.mobisoft.taskmanagement.repository.DepartmentRepository;
import com.mobisoft.taskmanagement.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DepartmentService {

    // @Autowired
    // private ObjectMapper objectMapper;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    public DepartmentDTO Adddepartments(DepartmentDTO departmentDTO) {
        Department department = convertToEntity(departmentDTO);
        Department savedDepartment = departmentRepository.save(department);
        return convertToDTO(savedDepartment);
    }
    
    public List<DepartmentDTO> findAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public DepartmentsResponse findAllDepartments2(int page, int size, String sortBy) {
        // Récupérer tous les départements
        List<Department> departments = departmentRepository.findAll();
        System.out.println(departments);
        
        // Convertir les départements en DepartmentDTO
        List<DepartmentDTO> departmentDTOs = departments.stream().map(this::convertToDTO).collect(Collectors.toList());
        // Utiliser votre paginator pour obtenir les éléments paginés
        Page<DepartmentDTO> pagedDepartments = Paginator.paginate(departmentDTOs, page, size, sortBy); // Assurez-vous que sortBy est utilisé pour le tri
    
        // Créer le DepartmentsResponse
        return new DepartmentsResponse(pagedDepartments.getContent(), pagedDepartments.getTotalElements(), pagedDepartments.getTotalPages());
    }
    
    
    public DepartmentDTO getDepartmentById(Long id) {
        Optional<Department> optionalDepartment = departmentRepository.findById(id);
            if (optionalDepartment.isPresent()) {
                return convertToDTO(optionalDepartment.get());
            } else {
                throw new EntityNotFoundException("Aucun département trouvé avec l'ID: " + id);
            }
    }


    // public List<?> getUsersByDepartmentId(Long departmentId) {
    //     Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new EntityNotFoundException("Le département avec l'ID spécifié n'existe pas"));
    //     System.out.println(department.getUsers().toString());
    //     return objectMapper.convertValue(department.getUsers(), List.class);
    // }

    // public List<User> getUsersByDepartmentId(Long departmentId) {
    //     return departmentRepository.findUsersByDepartmentId(departmentId);
    // }

    // public List<User> getUsersByDepartmentId(Long departmentId) {
    //     Department department = departmentRepository.findById(departmentId)
    //         .orElseThrow(() -> new RuntimeException("Département non trouvé avec l'ID : " + departmentId));
    //     return department.getUsers();
    // }

    public List<User> getUsersByDepartmentId(Long departmentId) {
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Département non trouvé avec l'ID : " + departmentId));
        return department.getUsers();
    }

    public List<User> getUsersByDepartmentIds(List<Long> departmentIds) {
        // Récupérer les départements basés sur les IDs
        List<Department> departments = departmentRepository.findAllById(departmentIds);

        // Vérifier si tous les départements ont été trouvés
        if (departments.size() != departmentIds.size()) {
            throw new RuntimeException("Un ou plusieurs départements non trouvés");
        }

        // Extraire tous les utilisateurs des départements
        Set<User> users = departments.stream()
                .flatMap(department -> department.getUsers().stream())
                .collect(Collectors.toSet());

        return new ArrayList<>(users); // Convertir en liste si nécessaire
    }

    



    public boolean deleteDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) { throw new EntityNotFoundException("Le département avec l'ID spécifié n'existe pas");}
        departmentRepository.deleteById(departmentId);
        return true;
    }

    /**
     * Associe un utilisateur à un département lors de l'inscription.
     */
    public void assignUserToDepartmentx(Long departmentId, Long userId) {

        System.out.println(userId);
        Department department = departmentRepository.findById(departmentId).orElseThrow(() -> new EntityNotFoundException("Le département avec l'ID spécifié n'existe pas"));
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas"));
        department.getUsers().add(user);
        departmentRepository.save(department);
    }

    
    public void assignUserToDepartment(Long departmentId, Long userId) {

        // Trouver le nouveau département et l'utilisateur
        Department newDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new EntityNotFoundException("Le département avec l'ID spécifié n'existe pas"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas"));
    
        // Trouver tous les départements auxquels l'utilisateur est actuellement associé
        List<Department> oldDepartments = departmentRepository.findDepartmentsOfUser(userId);
    
        // Supprimer l'utilisateur de tous ses anciens départements
        for (Department oldDepartment : oldDepartments) {
            if (!oldDepartment.getDepartmentId().equals(newDepartment.getDepartmentId())) {
                oldDepartment.getUsers().remove(user);
                user.getDepartments().remove(oldDepartment);
                // Sauvegarder les modifications
                departmentRepository.save(oldDepartment);
            }
        }
    
        // Ajouter l'utilisateur au nouveau département
        if (!newDepartment.getUsers().contains(user)) {
            newDepartment.getUsers().add(user);
        }
    
        // Ajouter le nouveau département à la liste des départements de l'utilisateur
        if (!user.getDepartments().contains(newDepartment)) {
            user.getDepartments().add(newDepartment);
        }
    
        // Sauvegarder les modifications dans le nouveau département et l'utilisateur
        departmentRepository.save(newDepartment);
        userRepository.save(user);
    
        System.out.println("Utilisateur ajouté au département avec succès.");
    }
    

    private DepartmentDTO convertToDTO(Department department) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setDepartmentId(department.getDepartmentId());
        departmentDTO.setDepartmentName(department.getDepartmentName());
        departmentDTO.setDepartmentSigle(department.getDepartmentSigle());
        departmentDTO.setDepartmentCreatedAt(department.getDepartmentCreatedAt());
        departmentDTO.setDepartmentUpdatedAt(department.getDepartmentUpdatedAt());
        // departmentDTO.setUsers(department.getUsers());
        // departmentDTO.setProjects(department.getProjects());
        return departmentDTO;
    }

    private Department convertToEntity(DepartmentDTO departmentDTO) {
        Department department = new Department();
        department.setDepartmentName(departmentDTO.getDepartmentName());
        department.setDepartmentSigle(departmentDTO.getDepartmentSigle());
        department.setDepartmentCreatedAt(OffsetDateTime.now());
        department.setDepartmentUpdatedAt(OffsetDateTime.now());
        return department;
    }


    private void updateDepartmentFromDTO(Department department, DepartmentDTO departmentDTO) {
        department.setDepartmentName(departmentDTO.getDepartmentName());
        department.setDepartmentSigle(departmentDTO.getDepartmentSigle());
        department.setDepartmentUpdatedAt(OffsetDateTime.now());
    }

}
