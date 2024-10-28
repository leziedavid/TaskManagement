package com.mobisoft.taskmanagement.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobisoft.taskmanagement.dto.UserProjectDTO;
import com.mobisoft.taskmanagement.entity.Project;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.entity.UserProject;
import com.mobisoft.taskmanagement.repository.ProjectRepository;
import com.mobisoft.taskmanagement.repository.UserProjectRepository;

import jakarta.persistence.EntityNotFoundException;


@Service
@Transactional
public class UserProjectService {

    @Autowired
    private UserProjectRepository userProjectRepository;

        @Autowired
    private ProjectRepository projectRepository;

    public UserProjectDTO createUserProject(Project project, UserProjectDTO userProjectDTO) {
        
        List<UserProject> userProjects = userProjectDTO.getUsersId().stream()
            .map((var userId) -> {

                User user = new User();
                user.setUserId(userId);

                UserProject userProject = new UserProject();
                userProject.setProject(project);
                userProject.setUser(user);
                userProject.setLeader(userId.equals(userProjectDTO.getLeaderId()));
                return userProject;
            })
            .collect(Collectors.toList());
            userProjectRepository.saveAll(userProjects);

            if (!userProjects.isEmpty()) {
                return convertToDTO(userProjects.get(0));
            }
            return null;
    }


    public UserProjectDTO getUserProjectById(Long userProjectId) {
        UserProject userProject = findUserProjectById(userProjectId);
        return convertToDTO(userProject);
    }

    public List<UserProjectDTO> findAllUserProjects() {
        List<UserProject> userProjects = userProjectRepository.findAll();
        return userProjects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public UserProjectDTO updateUserProject(Project project, UserProjectDTO userProjectDTO) {
        // Récupérer tous les UserProject existants pour ce projet
        List<UserProject> existingUserProjects = userProjectRepository.findByProjectId(project.getProjectId());
    
        // Liste pour stocker les UserProject mis à jour et nouveaux
        List<UserProject> updatedUserProjects = userProjectDTO.getUsersId().stream()
                .map(userId -> {
                    // Vérifier si l'utilisateur existe déjà dans les UserProject existants
                    Optional<UserProject> existingUserProjectOptional = existingUserProjects.stream()
                            .filter(up -> up.getUser().getUserId().equals(userId))
                            .findFirst();
    
                    UserProject userProject;
                    if (existingUserProjectOptional.isPresent()) {
                        // Si l'utilisateur existe déjà pour ce projet, mettre à jour ses informations
                        userProject = existingUserProjectOptional.get();
                    } else {
                        // Si l'utilisateur n'existe pas encore pour ce projet, créer un nouveau UserProject
                        userProject = new UserProject();
                        userProject.setProject(project);
                        User user = new User();
                        user.setUserId(userId);
                        userProject.setUser(user);
                    }
    
                    // Mettre à jour le statut de leader uniquement si c'est le leader désigné
                    userProject.setLeader(userId.equals(userProjectDTO.getLeaderId()));
                    return userProject;
                })
                .collect(Collectors.toList());
    
        // Enregistrer tous les UserProject (à la fois mis à jour et nouveaux)
        List<UserProject> savedUserProjects = userProjectRepository.saveAll(updatedUserProjects);
    
        // Retourner le DTO du premier UserProject mis à jour ou créé
        if (!savedUserProjects.isEmpty()) {
            return convertToDTO(savedUserProjects.get(0));
        }
    
        return null;
    }

    public UserProjectDTO updateUserProject2(Project project, UserProjectDTO userProjectDTO) {
        
        // Récupérer tous les UserProject existants pour ce projet
        List<UserProject> existingUserProjects = userProjectRepository.findByProjectId(project.getProjectId());

        // Mettre à jour les UserProject existants et ajouter de nouveaux si nécessaire
        List<UserProject> updatedUserProjects = userProjectDTO.getUsersId().stream()

            .map((var userId) -> {
                // Vérifier si l'utilisateur existe déjà dans les UserProject existants
                Optional<UserProject> existingUserProjectOptional = existingUserProjects.stream()
                    .filter(up -> up.getUser().getUserId().equals(userId))
                    .findFirst();

                UserProject userProject;
                if (existingUserProjectOptional.isPresent()) {

                    // Si l'utilisateur existe déjà, mettre à jour ses informations
                    userProject = existingUserProjectOptional.get();

                } else {
                    userProject = new UserProject();
                    userProject.setProject(project);
                    User user = new User();
                    user.setUserId(userId);
                    userProject.setUser(user);
                }
                
                userProject.setLeader(userId.equals(userProjectDTO.getLeaderId()));
                return userProject;
            })
            .collect(Collectors.toList());

        userProjectRepository.saveAll(updatedUserProjects);

        if (!updatedUserProjects.isEmpty()) {
            return convertToDTO(updatedUserProjects.get(0));
        }

        return null;
    }

    public boolean deleteUserProject(Long userProjectId) {
        UserProject userProject = findUserProjectById(userProjectId);
        userProjectRepository.delete(userProject);
        return true;
    }


    private UserProjectDTO convertToDTO(UserProject userProject) {
        UserProjectDTO userProjectDTO = new UserProjectDTO();
        userProjectDTO.setUsersId(List.of(userProject.getUser().getUserId()));
        userProjectDTO.setLeaderId(userProject.isLeader() ? userProject.getUser().getUserId() : null);
        return userProjectDTO;
    }

    private UserProject findUserProjectById(Long userProjectId) {
        return userProjectRepository.findById(userProjectId).orElseThrow(() -> new EntityNotFoundException("Association utilisateur-projet non trouvée avec l'ID: " + userProjectId));
    }


    // Ajouts de nouveau utilisateur:

    public UserProjectDTO assignUsersToProject(Project project, UserProjectDTO userProjectDTO) {
        // Récupérer tous les UserProject existants pour ce projet
        List<UserProject> existingUserProjects = userProjectRepository.findByProjectId(project.getProjectId());

        // Liste pour stocker les UserProject mis à jour et nouveaux
        List<UserProject> updatedUserProjects = userProjectDTO.getUsersId().stream()
                .map(userId -> {
                    // Vérifier si l'utilisateur existe déjà dans les UserProject existants
                    Optional<UserProject> existingUserProjectOptional = existingUserProjects.stream()
                            .filter(up -> up.getUser().getUserId().equals(userId))
                            .findFirst();

                    UserProject userProject;
                    if (existingUserProjectOptional.isPresent()) {
                        // Utilisateur déjà assigné au projet, ne rien faire ici
                        userProject = existingUserProjectOptional.get();
                    } else {
                        // Créer un nouveau UserProject si l'utilisateur n'existe pas déjà
                        User user = new User();
                        user.setUserId(userId);

                        userProject = new UserProject();
                        userProject.setProject(project);
                        userProject.setUser(user);
                        userProject.setLeader(userId.equals(userProjectDTO.getLeaderId()));

                        userProjectRepository.save(userProject);
                    }
                    return userProject;
                })
                .collect(Collectors.toList());

        if (!updatedUserProjects.isEmpty()) {
            return convertToDTO(updatedUserProjects.get(0));
        }
        return null;
    }


    public void removeUserFromProject(String projectId, Long userIdToRemove) {
        // Récupérer le UserProject spécifique à supprimer

        Project project = projectRepository.findByProjectCodes(projectId);
        
        if (project == null) {
            throw new EntityNotFoundException("Le projet avec le code spécifié n'existe pas : " + projectId);
        }
        Long IdProject = project.getProjectId();
        // System.out.println(IdProject);
        
        Optional<UserProject> userProjectOptional = userProjectRepository.findByProjectIdAndUserId(IdProject, userIdToRemove);

        // Vérifier si le UserProject existe et le supprimer
        userProjectOptional.ifPresent(userProjectRepository::delete);
    }


    public String updateProjectLeader(String projectId, Long currentLeaderId, Long newLeaderId) {

            Project project = projectRepository.findByProjectCodes(projectId);
            Long IdProject = project.getProjectId();
            System.out.println(IdProject);
            
            // Vérifier si le projet a déjà un leader
            List<UserProject> currentLeaderProjects = userProjectRepository.findByProjectIdAndLeader(IdProject, true);
            
            // Si le projet a déjà un leader, définir son champ leader à false
            if (!currentLeaderProjects.isEmpty()) {
                for (UserProject userProject : currentLeaderProjects) {
                    userProject.setLeader(false);
                    userProjectRepository.save(userProject);
                }
            }
            
            // Mettre à jour le nouveau leader
            Optional<UserProject> newLeaderProjectOptional = userProjectRepository.findByProjectIdAndUserId(IdProject, newLeaderId);

            if (newLeaderProjectOptional.isPresent()) {
                UserProject newLeaderProject = newLeaderProjectOptional.get();
                newLeaderProject.setLeader(true);
                userProjectRepository.save(newLeaderProject);
            } else {
                // Si le newLeaderId n'est pas associé au projet, vous pouvez gérer cela ici
                return "Utilisateur non associé au projet.";
            }
            
            return "Leader du projet mis à jour avec succès.";
        }

}
