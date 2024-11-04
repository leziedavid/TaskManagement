package com.mobisoft.taskmanagement.dto;

import java.util.List;

public class TaskDetailsDTO {

    private List<TaskDTO> tasks;          // Correction : devrait contenir les tâches, pas les actions
    private List<ActionDTO> actions;      // Correction : devrait contenir les actions, pas les tâches
    private List<ObservationDTO> observations;
    private List<UserDTO> assignedUsers;  // Nouvelle propriété pour les utilisateurs assignés
    private List<AbonnementDTO> abonnements; // Nouvelle propriété pour les abonnements

    public List<AbonnementDTO> getAbonnements() {
        return abonnements;
    }

    public void setAbonnements(List<AbonnementDTO> abonnements) {
        this.abonnements = abonnements;
    }
    // Getters and Setters

    /**
     * @return List<TaskDTO> return the tasks
     */
    public List<TaskDTO> getTasks() {
        return tasks;
    }

    /**
     * @param tasks the tasks to set
     */
    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    /**
     * @return List<ActionDTO> return the actions
     */
    public List<ActionDTO> getActions() {
        return actions;
    }

    /**
     * @param actions the actions to set
     */
    public void setActions(List<ActionDTO> actions) {
        this.actions = actions;
    }

    /**
     * @return List<ObservationDTO> return the observations
     */
    public List<ObservationDTO> getObservations() {
        return observations;
    }

    /**
     * @param observations the observations to set
     */
    public void setObservations(List<ObservationDTO> observations) {
        this.observations = observations;
    }

    /**
     * @return List<UserDTO> return the assignedUsers
     */
    public List<UserDTO> getAssignedUsers() {
        return assignedUsers;
    }

    /**
     * @param assignedUsers the assignedUsers to set
     */
    public void setAssignedUsers(List<UserDTO> assignedUsers) {
        this.assignedUsers = assignedUsers;
    }
}
