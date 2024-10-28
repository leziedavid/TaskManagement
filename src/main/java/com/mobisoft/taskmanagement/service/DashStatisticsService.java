package com.mobisoft.taskmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.taskmanagement.dto.GlobalStatsDTO;
import com.mobisoft.taskmanagement.dto.UserRoleDTO;
import com.mobisoft.taskmanagement.entity.Gender;
import com.mobisoft.taskmanagement.entity.Project;
import com.mobisoft.taskmanagement.entity.Role;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.entity.Task;
import com.mobisoft.taskmanagement.repository.DepartmentRepository;
import com.mobisoft.taskmanagement.repository.ProjectRepository;
import com.mobisoft.taskmanagement.repository.TaskRepository;
import com.mobisoft.taskmanagement.repository.UserRepository;

@Service
public class DashStatisticsService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserService userService; // Injection de UserService


    public GlobalStatsDTO getGlobalStats(String token , Long Iduser) {
        GlobalStatsDTO stats = new GlobalStatsDTO();
        // Obtenez les informations utilisateur à partir du token
        UserRoleDTO userRoleDTO;
        // Obtenez les informations utilisateur à partir du token ou de userId
        if (Iduser != null) {
            userRoleDTO = userService.getUserRoleAndIdFromUserId(Iduser);
        } else {
            userRoleDTO = userService.getUserRoleAndIdFromToken(token);
        }
        // Utilisez l'ID et le rôle de l'utilisateur comme nécessaire
        Long userId = userRoleDTO.getUserId();
        Role role = userRoleDTO.getRole();
    
        try {
            if (role == Role.ADMIN) {
                // Project statistics
                stats.setTotalProjectsInProgress((int) projectRepository.countByProjectState(State.EN_COURS));
                stats.setTotalProjectsPending((int) projectRepository.countByProjectState(State.EN_ATTENTE));
                stats.setTotalProjectsCompleted((int) projectRepository.countByProjectState(State.TERMINER));
    
                // Task statistics
                stats.setTotalTasksInProgress((int) taskRepository.countByTaskState(State.EN_COURS));
                stats.setTotalTasksPending((int) taskRepository.countByTaskState(State.EN_ATTENTE));
                stats.setTotalTasksCompleted((int) taskRepository.countByTaskState(State.TERMINER));
    
                // Total counts
                stats.setTotalProjects((int) projectRepository.count());
                stats.setTotalTasks((int) taskRepository.count());
    
                // Gender statistics
                stats.setTotalDepartments((int) departmentRepository.count());
                stats.setTotalUsers((int) userRepository.count());
                stats.setTotalWomen((int) userRepository.countByGenre(Gender.FEMME));
                stats.setTotalMen((int) userRepository.countByGenre(Gender.HOMME));
            } else {
                // Retrieve projects and tasks for specific user
                List<Project> projects = projectRepository.findProjectsByUserId(userId);
                List<Task> tasks = taskRepository.findByAssigned_UserId(userId);
    
                // Apply filters for projects
                long inProgressProjects = projects.stream().filter(p -> p.getProjectState().equals(State.EN_COURS)).count();
                long pendingProjects = projects.stream().filter(p -> p.getProjectState().equals(State.EN_ATTENTE)).count();
                long completedProjects = projects.stream().filter(p -> p.getProjectState().equals(State.TERMINER)).count();
    
                stats.setTotalProjectsInProgress((int) inProgressProjects);
                stats.setTotalProjectsPending((int) pendingProjects);
                stats.setTotalProjectsCompleted((int) completedProjects);
    
                // Apply filters for tasks
                long inProgressTasks = tasks.stream().filter(t -> t.getTaskState().equals(State.EN_COURS)).count();
                long pendingTasks = tasks.stream().filter(t -> t.getTaskState().equals(State.EN_ATTENTE)).count();
                long completedTasks = tasks.stream().filter(t -> t.getTaskState().equals(State.TERMINER)).count();
    
                stats.setTotalTasksInProgress((int) inProgressTasks);
                stats.setTotalTasksPending((int) pendingTasks);
                stats.setTotalTasksCompleted((int) completedTasks);
    
                // Total counts
                stats.setTotalProjects((int) projects.size());
                stats.setTotalTasks((int) tasks.size());
    
                // Gender statistics (for all users)
                stats.setTotalDepartments((int) departmentRepository.count());
                stats.setTotalUsers((int) userRepository.count());
                stats.setTotalWomen((int) userRepository.countByGenre(Gender.FEMME));
                stats.setTotalMen((int) userRepository.countByGenre(Gender.HOMME));
            }
        } catch (Exception e) {
            // Log the exception
            System.err.println("Error while retrieving global statistics: " + e.getMessage());
            // You might want to set some default values or rethrow the exception
        }
    
        return stats;
    }


    public GlobalStatsDTO getProjectStatistics(String token,Long Iduser) {

        try {

            // // Obtenez les informations utilisateur à partir du token
            // UserRoleDTO userRoleDTO = userService.getUserRoleAndIdFromToken(token);
            UserRoleDTO userRoleDTO;
            // Obtenez les informations utilisateur à partir du token ou de userId
            if (Iduser != null) {
                userRoleDTO = userService.getUserRoleAndIdFromUserId(Iduser);
            } else {
                userRoleDTO = userService.getUserRoleAndIdFromToken(token);
            }
            // Utilisez l'ID et le rôle de l'utilisateur comme nécessaire
            Long userId = userRoleDTO.getUserId();
            Role role = userRoleDTO.getRole();

            GlobalStatsDTO stats = new GlobalStatsDTO();

            if (role == Role.USER) {
                
                // Statistiques pour un utilisateur spécifique
                long totalProjects = projectRepository.countProjectsByUserId(userId);
                long totalProjectsInProgress = projectRepository.countProjectsByUserIdAndState(userId, State.EN_COURS);
                long totalProjectsPending = projectRepository.countProjectsByUserIdAndState(userId, State.EN_ATTENTE);
                long totalProjectsCompleted = projectRepository.countProjectsByUserIdAndState(userId, State.TERMINER);

                stats.setTotalProjects((int) totalProjects);
                stats.setTotalProjectsInProgress((int) totalProjectsInProgress);
                stats.setTotalProjectsPending((int) totalProjectsPending);
                stats.setTotalProjectsCompleted((int) totalProjectsCompleted);

            } else {
                // Statistiques pour tous les projets
                long totalProjects = projectRepository.countAllProjects();
                long totalProjectsInProgress = projectRepository.countByState(State.EN_COURS);
                long totalProjectsPending = projectRepository.countByState(State.EN_ATTENTE);
                long totalProjectsCompleted = projectRepository.countByState(State.TERMINER);

                stats.setTotalProjects((int) totalProjects);
                stats.setTotalProjectsInProgress((int) totalProjectsInProgress);
                stats.setTotalProjectsPending((int) totalProjectsPending);
                stats.setTotalProjectsCompleted((int) totalProjectsCompleted);
            }

            return stats;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération des statistiques des projets: " + e.getMessage());
        }
    }

}
