package com.mobisoft.taskmanagement.service;

import com.mobisoft.taskmanagement.dto.ProjectDTO;
import com.mobisoft.taskmanagement.entity.Project;
import com.mobisoft.taskmanagement.entity.Priority;
import com.mobisoft.taskmanagement.entity.State;
import com.mobisoft.taskmanagement.repository.ProjectRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    public ProjectDTO createProject(ProjectDTO projectDTO) {
        try {
            Project project = convertToEntity(projectDTO);
            Project savedProject = projectRepository.save(project);
            return convertToDTO(savedProject);
        } catch (Exception e) {
            throw new EntityNotFoundException("Erreur lors de la création du projet: " + e.getMessage());
        }
    }

    public ProjectDTO getProjectById(Long projectId) {
        Optional<Project> optionalProject = projectRepository.findById(projectId);
        return optionalProject.map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Aucun projet trouvé avec l'ID: " + projectId));
    }

    public List<ProjectDTO> findAllProjects() {
        try {
            List<Project> projects = projectRepository.findAll();
            return projects.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de tous les projets: " + e.getMessage());
        }
    }

    public ProjectDTO updateProject(Long projectId, ProjectDTO projectDTO) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Le projet avec l'ID spécifié n'existe pas"));
        updateProjectFromDTO(project, projectDTO);
        Project updatedProject = projectRepository.save(project);
        return convertToDTO(updatedProject);
    }

    public boolean deleteProject(Long projectId) {
        if (!projectRepository.existsById(projectId)) {
            throw new EntityNotFoundException("Le projet avec l'ID spécifié n'existe pas");
        }
        projectRepository.deleteById(projectId);
        return true;
    }

    private ProjectDTO convertToDTO(Project project) {
        
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setProjectState(project.getProjectState().name());
        projectDTO.setProjectPriority(project.getProjectPriority().name());
        // projectDTO.setProjectState(project.getProjectState());
        // projectDTO.setProjectPriority(project.getProjectPriority());
        projectDTO.setProjectDescription(project.getProjectDescription());
        LocalDate projectStartDate = project.getProjectStartDate();
        String projectStartDateAsString = projectStartDate.toString();
        projectDTO.setProjectStartDate(projectStartDateAsString);
        LocalDate projectEndDate = project.getProjectEndDate();
        String projectEndDateAsString = projectEndDate.toString();
        projectDTO.setProjectEndDate(projectEndDateAsString);

        // projectDTO.setProjectStartDate(project.getProjectStartDate());
        // projectDTO.setProjectEndDate(project.getProjectEndDate());
        return projectDTO;
    }

    private Project convertToEntity(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setProjectName(projectDTO.getProjectName());

        project.setProjectState(State.valueOf(projectDTO.getProjectState()));
        // project.setProjectState(projectDTO.getProjectState());
        project.setProjectPriority(Priority.valueOf(projectDTO.getProjectPriority()));
        // project.setProjectPriority(projectDTO.getProjectPriority());
        
        project.setProjectDescription(projectDTO.getProjectDescription());
        LocalDate projectStartDate = LocalDate.parse(projectDTO.getProjectStartDate());
        project.setProjectStartDate(projectStartDate);
        LocalDate projectEndDate = LocalDate.parse(projectDTO.getProjectEndDate());
        project.setProjectEndDate(projectEndDate);

        // project.setProjectStartDate(projectDTO.getProjectStartDate());
        // project.setProjectEndDate(projectDTO.getProjectEndDate());
        return project;
    }

    private void updateProjectFromDTO(Project project, ProjectDTO projectDTO) {

        project.setProjectName(projectDTO.getProjectName());

        project.setProjectState(State.valueOf(projectDTO.getProjectState()));
        project.setProjectPriority(Priority.valueOf(projectDTO.getProjectPriority()));
        // project.setProjectState(projectDTO.getProjectState());
        // project.setProjectPriority(projectDTO.getProjectPriority());
        project.setProjectDescription(projectDTO.getProjectDescription());
        LocalDate projectStartDate = LocalDate.parse(projectDTO.getProjectStartDate());
        project.setProjectStartDate(projectStartDate);
        LocalDate projectEndDate = LocalDate.parse(projectDTO.getProjectEndDate());
        project.setProjectEndDate(projectEndDate);

        // project.setProjectStartDate(projectDTO.getProjectStartDate());
        // project.setProjectEndDate(projectDTO.getProjectEndDate());

    }
}
