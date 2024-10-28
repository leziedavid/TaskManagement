package com.mobisoft.taskmanagement.entity;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "leaves")
@Data
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long leaveId;

    // @ManyToOne
    // @JoinColumn(name = "user_id", nullable = false)
    // private User user;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @Column(name = "start_date", nullable = false)
    private OffsetDateTime startDate;

    @Column(name = "end_date", nullable = false)
    private OffsetDateTime endDate;

    @Column(name = "leave_type", nullable = false)
    private String leaveType; // Par exemple : "annual", "sick", "maternity", etc.

    @Column(name = "status", nullable = false)
    private String status; // Par exemple : "approved", "pending", "rejected"

    @Column(name = "description")
    private String description; // Optionnel, pour ajouter des détails supplémentaires

    public Leave() {
    }
}
