package com.mobisoft.taskmanagement.dto;

import lombok.Data;

@Data
public class GlobalStatsDTO {
    private int totalProjectsInProgress;
    private int totalProjectsPending;
    private int totalProjectsCompleted;
    private int totalTasksInProgress;
    private int totalTasksPending;
    private int totalTasksCompleted;
    private int totalUsers; // Ajouté pour le nombre total d'utilisateurs
    private int totalProjects; // Ajouté pour le nombre total de projets
    private int totalTasks; // Ajouté pour le nombre total de tâches
    private int totalDepartments; // Ajouté pour le nombre total de départements

    private int totalWomen; // Ajouté pour le nombre total de femmes
    private int totalMen;   // Ajouté pour le nombre total d'hommes
}
