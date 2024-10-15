package com.mobisoft.taskmanagement.service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobisoft.taskmanagement.dto.LeaveDTO;
import com.mobisoft.taskmanagement.entity.Leave;
import com.mobisoft.taskmanagement.entity.User;
import com.mobisoft.taskmanagement.repository.LeaveRepository;
import com.mobisoft.taskmanagement.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private UserRepository userRepository;

    public LeaveDTO createLeave(LeaveDTO leaveDto) {
        Leave leave = convertToEntity(leaveDto);
        Leave savedLeave = leaveRepository.save(leave);
        return convertToDto(savedLeave);
    }

    public LeaveDTO getLeaveById(Long leaveId) {
        Optional<Leave> optionalLeave = leaveRepository.findById(leaveId);
        return optionalLeave.map(this::convertToDto)
                .orElseThrow(() -> new EntityNotFoundException("Aucun congé trouvé avec l'ID: " + leaveId));
    }

    public List<LeaveDTO> findAllLeaves() {
        List<Leave> leaves = leaveRepository.findAll();
        return leaves.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public LeaveDTO updateLeave(Long leaveId, LeaveDTO leaveDto) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new EntityNotFoundException("Le congé avec l'ID spécifié n'existe pas"));
        updateLeaveFromDto(leave, leaveDto);
        Leave updatedLeave = leaveRepository.save(leave);
        return convertToDto(updatedLeave);
    }

    public boolean deleteLeave(Long leaveId) {
        if (!leaveRepository.existsById(leaveId)) {
            throw new EntityNotFoundException("Le congé avec l'ID spécifié n'existe pas");
        }
        leaveRepository.deleteById(leaveId);
        return true;
    }

    public List<LeaveDTO> getLeavesByUserId(Long userId) {
        List<Leave> leaves = leaveRepository.findByUser_UserId(userId);
        return leaves.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private LeaveDTO convertToDto(Leave leave) {
        LeaveDTO leaveDto = new LeaveDTO();
        leaveDto.setLeaveId(leave.getLeaveId());
        leaveDto.setUserId(leave.getUser().getUserId());

        // Convertir les dates en OffsetDateTime
        leaveDto.setStartDate(leave.getStartDate());
        leaveDto.setEndDate(leave.getEndDate());

        leaveDto.setLeaveType(leave.getLeaveType());
        leaveDto.setStatus(leave.getStatus());
        leaveDto.setDescription(leave.getDescription());

        return leaveDto;
    }

    private Leave convertToEntity(LeaveDTO leaveDto) {
        Leave leave = new Leave();
        leave.setLeaveId(leaveDto.getLeaveId());
        leave.setLeaveType(leaveDto.getLeaveType());
        leave.setStatus(leaveDto.getStatus());
        leave.setDescription(leaveDto.getDescription());

        // Convertir les dates en OffsetDateTime
        leave.setStartDate(leaveDto.getStartDate());
        leave.setEndDate(leaveDto.getEndDate());

        if (leaveDto.getUserId() != null) {
            User user = userRepository.findById(leaveDto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas"));
            leave.setUser(user);
        }

        return leave;
    }

    private void updateLeaveFromDto(Leave leave, LeaveDTO leaveDto) {
        leave.setLeaveType(leaveDto.getLeaveType());
        leave.setStatus(leaveDto.getStatus());
        leave.setDescription(leaveDto.getDescription());

        // Convertir les dates en OffsetDateTime
        leave.setStartDate(leaveDto.getStartDate());
        leave.setEndDate(leaveDto.getEndDate());

        if (leaveDto.getUserId() != null) {
            User user = userRepository.findById(leaveDto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("L'utilisateur avec l'ID spécifié n'existe pas"));
            leave.setUser(user);
        }
    }

    public LeaveDTO updateLeaveStatus(Long leaveId, String status) {
        Leave leave = leaveRepository.findById(leaveId)
                .orElseThrow(() -> new RuntimeException("Congé non trouvé"));
        leave.setStatus(status);
        Leave updatedLeave = leaveRepository.save(leave);
        return convertToDto(updatedLeave);
    }
}
