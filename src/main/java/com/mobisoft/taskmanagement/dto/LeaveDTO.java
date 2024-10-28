package com.mobisoft.taskmanagement.dto;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LeaveDTO {

    private Long leaveId;
    private Long userId; // Identifiant de l'utilisateur
    private OffsetDateTime startDate;
    private OffsetDateTime endDate;
    private String leaveType;
    private String status;
    private String description;

}
