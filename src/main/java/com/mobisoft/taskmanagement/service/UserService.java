package com.mobisoft.taskmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mobisoft.taskmanagement.dto.UserDTO;
import com.mobisoft.taskmanagement.entity.Gender;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO createUser(UserDTO userDTO) {

        try {
            User user = convertToEntity(userDTO);
            User savedUser = userRepository.save(user);
            return convertToDTO(savedUser);

        } catch (Exception e) {
            throw new EntityNotFoundException("Erreur lors de la création de l'utilisateur: " + e.getMessage());
        }
    }

    public UserDTO getUserById(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.map(this::convertToDTO)
            .orElseThrow(() -> new EntityNotFoundException("Aucun utilisateur trouvé avec l'ID: " + userId));
    }

    public List<UserDTO> findAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de tous les utilisateurs: " + e.getMessage());
        }
    }

    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas"));
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

    private UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setLastname(user.getLastname());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setPhone(user.getPhone());
        userDTO.setEmail(user.getEmail());
        userDTO.setUsername(user.getUsername());
        // userDTO.setPassword(user.getPassword());
        userDTO.setFonction(user.getFonction());
        userDTO.setGenre(user.getGenre().name());
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
        // System.out.println(passwordEncoder.encode(userDTO.getPassword()));
        user.setFonction(userDTO.getFonction());
        user.setGenre(Gender.valueOf(userDTO.getGenre()));
        return user;
    }

    private void updateUserFromDTO(User user, UserDTO userDTO) {
        user.setLastname(userDTO.getLastname());
        user.setFirstname(userDTO.getFirstname());
        user.setPhone(userDTO.getPhone());
        user.setEmail(userDTO.getEmail());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setFonction(userDTO.getFonction());
        user.setGenre(Gender.valueOf(userDTO.getGenre()));
    }
}
