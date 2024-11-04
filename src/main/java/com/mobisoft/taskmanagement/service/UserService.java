package com.mobisoft.taskmanagement.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service; // Importer l'énumération Role
import org.springframework.web.multipart.MultipartFile;

import com.mobisoft.taskmanagement.dto.LeaveDTO;
import com.mobisoft.taskmanagement.dto.UserDTO;
import com.mobisoft.taskmanagement.dto.UserDepartmentDTO;
import com.mobisoft.taskmanagement.dto.UserRoleDTO;
import com.mobisoft.taskmanagement.dto.UsersResponse;
import com.mobisoft.taskmanagement.entity.Gender;
import com.mobisoft.taskmanagement.entity.Leave;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.exception.EmailAlreadyExistsException;
import com.mobisoft.taskmanagement.pagination.Page;
import com.mobisoft.taskmanagement.pagination.Paginator;
import com.mobisoft.taskmanagement.repository.DepartmentRepository;
import com.mobisoft.taskmanagement.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DepartmentService departmentService;


    @Autowired
    private FileStorageService fileStorageService;

    // private final String uploadDir = "C:\\Users\\MOBISOFT_012\\Desktop\\TDLLEZIE\\TaskBakend\\Profil";
    // private final String uploadDir = "/Users/osx/Desktop/task-management/Profil";

    private final String uploadDir = "/home/mohamed/mobitask/TaskManagement";
    private final String uploadProfilDir = "=/home/mohamed/mobitask/TaskManagement";

    public UserDTO addUsers(UserDTO userDTO, MultipartFile profil) {
        // Vérification si l'email existe déjà

        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new EmailAlreadyExistsException(userDTO.getEmail());
        }

        User user = convertToEntity(userDTO);
        // Gestion de la photo de profil
        if (profil != null && !profil.isEmpty()) {
            
            try {

                String publicId = fileStorageService.uploadImage(profil);
                user.setProfil(publicId);

                // String newFileName = saveFile(profil);
                // user.setProfil(newFileName);

            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la gestion du fichier: " + e.getMessage());
            }
        }

        User savedUser = userRepository.save(user);
        departmentService.assignUserToDepartment(userDTO.getDepartmentId(),savedUser.getUserId());

        return convertToDTO(savedUser);

    }

    public UserDTO updateUser(Long userId, UserDTO userDTO, MultipartFile profil) {
        User existingUser = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé"));

        updateUserFromDTO(existingUser, userDTO);

        // Gestion de la photo de profil
        if (profil != null && !profil.isEmpty()) {
            try {
                
                String publicId = fileStorageService.uploadImage(profil);
                existingUser.setProfil(publicId);

                // String newFileName = saveFile(profil);
                // existingUser.setProfil(newFileName);
            
            } catch (IOException e) {
                throw new RuntimeException("Erreur lors de la gestion du fichier: " + e.getMessage());
            }
        }

        User updatedUser = userRepository.save(existingUser);
        departmentService.assignUserToDepartment(userDTO.getDepartmentId(),updatedUser.getUserId());
        
        return convertToDTO(updatedUser);

    }

    private void updateUserFromDTO(User user, UserDTO userDTO) {

        user.setLastname(userDTO.getLastname());
        user.setFirstname(userDTO.getFirstname());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());

        // if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
        //     user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        // }
        user.setFonction(userDTO.getFonction());
        user.setGenre(Gender.valueOf(userDTO.getGenre()));
        user.setRole(userDTO.getRole());

        // Si besoin, ajouter des champs manquants ici
    }

    private String saveFile(MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Assurer que le répertoire d'upload existe, sinon le créer
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        // Générer un UUID aléatoire pour le nom du fichier
        String uuid = UUID.randomUUID().toString();

        // Obtenir l'extension du fichier d'origine
        String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
        String fileExtension = FilenameUtils.getExtension(fileName);

        // Nouveau nom de fichier avec UUID + extension
        String newFileName = uuid + "." + fileExtension;
        // Chemin complet du fichier à enregistrer
        Path filePath = uploadPath.resolve(newFileName);
        // Enregistrer le fichier dans le répertoire
        Files.copy(file.getInputStream(), filePath);
        // System.out.println(filePath);
        // return newFileName;
        return filePath.toString();
    }

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setLastname(user.getLastname());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setPhone(user.getPhone());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        userDTO.setFonction(user.getFonction());
        userDTO.setGenre(user.getGenre().name()); // Supposer que `UserDTO` utilise le nom de l'enum pour le genre
        userDTO.setRole(user.getRole());
        userDTO.setProfil(user.getProfil()); // Nom du fichier de la photo de profil
        return userDTO;
    }

    private User convertToEntity(UserDTO userDTO) {

        User user = new User();
        user.setLastname(userDTO.getLastname());
        user.setFirstname(userDTO.getFirstname());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setFonction(userDTO.getFonction());
        user.setGenre(Gender.valueOf(userDTO.getGenre()));
        user.setRole(userDTO.getRole()); // Ajout du rôle
        return user;
    }

    public UserDTO getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.map(this::convertToDTO).orElseThrow(() -> new EntityNotFoundException("Aucun utilisateur trouvé avec l'ID: " + userId));
    }

    public List<UserDTO> findAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return users.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de tous les utilisateurs: " + e.getMessage());
        }
    }

    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas"));
        updateUserFromDTO(user, userDTO);
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }

    public boolean deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas");
        }
        userRepository.deleteById(userId);
        return true;
    }

    public UserDTO updateUserStatus(Long userId, Integer isValides) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Utilisateur non trouvée"));
        user.setIsValides(isValides);
        User updatedAction = userRepository.save(user);
        return convertToDTO(updatedAction);
    }

    public List<UserDepartmentDTO> getAllUsersWithDepartments() {
        List<User> users = userRepository.findAll();

        return users.stream().map(user -> {
            UserDepartmentDTO dto = new UserDepartmentDTO();
            dto.setUserId(user.getUserId());
            dto.setLastname(user.getLastname());
            dto.setFirstname(user.getFirstname());
            dto.setPhone(user.getPhone());
            dto.setEmail(user.getEmail());
            dto.setUsername(user.getUsername());
            dto.setFonction(user.getFonction());
            dto.setToken(user.getToken());
            dto.setOtp(user.getOtp());
            dto.setIsValid(user.getIsValides());
            dto.setGenre(user.getGenre());
            dto.setRole(user.getRole()); // Ajout du rôle
            
            // Mapping the departments
            dto.setDepartments(user.getDepartments().stream().map(department -> {
                UserDepartmentDTO.DepartmentDTO departmentDTO = new UserDepartmentDTO.DepartmentDTO();
                departmentDTO.setDepartmentId(department.getDepartmentId());
                departmentDTO.setDepartmentName(department.getDepartmentName());
                return departmentDTO;
            }).collect(Collectors.toList()));

             // Finding the most recent leave and applying status check
            Leave mostRecentLeave = user.getLeaves().stream()
                .max((leave1, leave2) -> leave1.getStartDate().compareTo(leave2.getStartDate()))
                .orElse(null);
    
            if (mostRecentLeave != null && !"completed".equalsIgnoreCase(mostRecentLeave.getStatus())) {
                LeaveDTO leaveDTO = new LeaveDTO();
                leaveDTO.setLeaveId(mostRecentLeave.getLeaveId());
                leaveDTO.setUserId(mostRecentLeave.getUser().getUserId());
                leaveDTO.setStartDate(mostRecentLeave.getStartDate());
                leaveDTO.setEndDate(mostRecentLeave.getEndDate());
                leaveDTO.setLeaveType(mostRecentLeave.getLeaveType());
                leaveDTO.setStatus(mostRecentLeave.getStatus());
                leaveDTO.setDescription(mostRecentLeave.getDescription());
                dto.setLeaves(Collections.singletonList(leaveDTO));
            } else {
                dto.setLeaves(Collections.emptyList());
            }
    

            return dto;
        }).collect(Collectors.toList());

    }

    public UsersResponse getAllUsersWithDepartments2(int page, int size, String sortBy) {
        // Récupérer tous les utilisateurs
        List<User> users = userRepository.findAll();

        // Convertir les utilisateurs en UserDepartmentDTO
        List<UserDepartmentDTO> userDepartmentDTOs = users.stream().map(user -> {
            UserDepartmentDTO dto = new UserDepartmentDTO();
            dto.setUserId(user.getUserId());
            dto.setLastname(user.getLastname());
            dto.setFirstname(user.getFirstname());
            dto.setPhone(user.getPhone());
            dto.setEmail(user.getEmail());
            dto.setUsername(user.getUsername());
            dto.setFonction(user.getFonction());
            dto.setToken(user.getToken());
            dto.setOtp(user.getOtp());
            dto.setIsValid(user.getIsValides());
            dto.setGenre(user.getGenre());
            dto.setRole(user.getRole());

            // Mapping des départements
            dto.setDepartments(user.getDepartments().stream().map(department -> {
                UserDepartmentDTO.DepartmentDTO departmentDTO = new UserDepartmentDTO.DepartmentDTO();
                departmentDTO.setDepartmentId(department.getDepartmentId());
                departmentDTO.setDepartmentName(department.getDepartmentName());
                return departmentDTO;
            }).collect(Collectors.toList()));

            // Récupérer le congé le plus récent
            Leave mostRecentLeave = user.getLeaves().stream()
                    .max((leave1, leave2) -> leave1.getStartDate().compareTo(leave2.getStartDate()))
                    .orElse(null);

            if (mostRecentLeave != null && !"completed".equalsIgnoreCase(mostRecentLeave.getStatus())) {
                LeaveDTO leaveDTO = new LeaveDTO();
                leaveDTO.setLeaveId(mostRecentLeave.getLeaveId());
                leaveDTO.setUserId(mostRecentLeave.getUser().getUserId());
                leaveDTO.setStartDate(mostRecentLeave.getStartDate());
                leaveDTO.setEndDate(mostRecentLeave.getEndDate());
                leaveDTO.setLeaveType(mostRecentLeave.getLeaveType());
                leaveDTO.setStatus(mostRecentLeave.getStatus());
                leaveDTO.setDescription(mostRecentLeave.getDescription());
                dto.setLeaves(Collections.singletonList(leaveDTO));
            } else {
                dto.setLeaves(Collections.emptyList());
            }

            return dto;
        }).collect(Collectors.toList());

        // Utiliser votre paginator pour obtenir les éléments paginés
        Page<UserDepartmentDTO> pagedUsers = Paginator.paginate(userDepartmentDTOs, page, size, sortBy);

        // Créer le UsersResponse
        return new UsersResponse(pagedUsers.getContent(), pagedUsers.getTotalElements(), pagedUsers.getTotalPages());
    }


    public UserDepartmentDTO getUserByIdWithDepartments(Long userId) {
        // Trouver l'utilisateur par ID
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isPresent()) {

            User user = optionalUser.get();
            UserDepartmentDTO dto = new UserDepartmentDTO();
            dto.setUserId(user.getUserId());
            dto.setLastname(user.getLastname());
            dto.setFirstname(user.getFirstname());
            dto.setPhone(user.getPhone());
            dto.setEmail(user.getEmail());
            dto.setUsername(user.getUsername());
            dto.setFonction(user.getFonction());
            dto.setToken(user.getToken());
            dto.setOtp(user.getOtp());
            dto.setIsValid(user.getIsValides());
            dto.setGenre(user.getGenre());
            dto.setRole(user.getRole()); // Ajout du rôle
            dto.setProfil(user.getProfil());

            // Assurez-vous que le champ `usersCreatedAt` dans UserDepartmentDTO est de type String
            OffsetDateTime usersCreatedAt = user.getUsersCreatedAt();
            if (usersCreatedAt != null) {
                String usersCreatedAtAsString = usersCreatedAt.toString();
                dto.setUsersCreatedAt(usersCreatedAtAsString);
            } else {
                dto.setUsersCreatedAt(null);
            
    }

            // Mapping the departments
            dto.setDepartments(user.getDepartments().stream().map(department -> {
                UserDepartmentDTO.DepartmentDTO departmentDTO = new UserDepartmentDTO.DepartmentDTO();
                departmentDTO.setDepartmentId(department.getDepartmentId());
                departmentDTO.setDepartmentName(department.getDepartmentName());
                return departmentDTO;
            }).collect(Collectors.toList()));

            return dto;
            
        } else {
            // Gérer le cas où l'utilisateur n'est pas trouvé
            // Vous pouvez lancer une exception, retourner null, ou créer un DTO vide selon vos besoins
            throw new RuntimeException("User not found with ID: " + userId);
        }
    }

    /**
     * Méthode pour obtenir le rôle et l'ID de l'utilisateur à partir du token.
     *
     * @param token Le token JWT de l'utilisateur.
     * @return Un UserRoleDTO contenant l'ID et le rôle de l'utilisateur.
     */

    public UserRoleDTO getUserRoleAndIdFromToken(String token) {
        // Récupérer l'utilisateur à partir du token
        User user = userRepository.findByToken(token).orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé pour le token : " + token));
        // Construire le UserRoleDTO
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(user.getUserId());
        userRoleDTO.setRole(user.getRole());
        return userRoleDTO;
    }

    public UserRoleDTO getUserRoleAndIdFromUserId(Long userId) {
        // Récupérer l'utilisateur à partir de l'ID utilisateur
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("Utilisateur non trouvé pour l'ID : " + userId));
        // Construire le UserRoleDTO
        UserRoleDTO userRoleDTO = new UserRoleDTO();
        userRoleDTO.setUserId(user.getUserId());
        userRoleDTO.setRole(user.getRole());
        return userRoleDTO;
    }

    // public List<String> getEmailsByIds(List<Long> userIds) {
    //     return userRepository.findEmailsByIds(userIds);
    // }


    public List<String> getEmailsByIds(List<Long> userIds) {
        // Vérifiez si la liste des IDs n'est pas vide
        if (userIds == null || userIds.isEmpty()) {
            return List.of(); // Retourne une liste vide si aucune ID n'est fournie
        }
        return userRepository.findEmailsByIds(userIds);
    }

    public String getEmailById(Long userId) {
        // Vérifiez si l'ID de l'utilisateur n'est pas nul
        if (userId == null) {
            return null; // Retourne null si l'ID n'est pas fourni
        }
        return userRepository.findEmailById(userId); // Méthode à définir dans UserRepository
    }
    public String findNameById(Long userId) {
        // Vérifiez si l'ID de l'utilisateur n'est pas nul
        if (userId == null) {
            return null; // Retourne null si l'ID n'est pas fourni
        }
        return userRepository.findFullNameById(userId); // Méthode à définir dans UserRepository
    }
    

}
