package com.mobisoft.taskmanagement.service;

import com.mobisoft.taskmanagement.dto.GroupDTO;
import com.mobisoft.taskmanagement.entity.Group;
import com.mobisoft.taskmanagement.repository.GroupRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    public GroupDTO createGroup(GroupDTO groupDTO) {
        try {
            Group group = convertToEntity(groupDTO);
            Group savedGroup = groupRepository.save(group);
            return convertToDTO(savedGroup);
        } catch (Exception e) {
            throw new EntityNotFoundException("Erreur lors de la création du groupe: " + e.getMessage());
        }
    }

    public GroupDTO getGroupById(Long groupId) {
        Optional<Group> optionalGroup = groupRepository.findById(groupId);
        return optionalGroup.map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Aucun groupe trouvé avec l'ID: " + groupId));
    }

    public List<GroupDTO> findAllGroups() {
        try {
            List<Group> groups = groupRepository.findAll();
            return groups.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de tous les groupes: " + e.getMessage());
        }
    }

    public GroupDTO updateGroup(Long groupId, GroupDTO groupDTO) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("Le groupe avec l'ID spécifié n'existe pas"));
        updateGroupFromDTO(group, groupDTO);
        Group updatedGroup = groupRepository.save(group);
        return convertToDTO(updatedGroup);
    }

    public boolean deleteGroup(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new EntityNotFoundException("Le groupe avec l'ID spécifié n'existe pas");
        }
        groupRepository.deleteById(groupId);
        return true;
    }

    private GroupDTO convertToDTO(Group group) {
        GroupDTO groupDTO = new GroupDTO();
        // groupDTO.setGroupId(group.getGroupId());
        groupDTO.setGroupName(group.getGroupName());
        return groupDTO;
    }

    private Group convertToEntity(GroupDTO groupDTO) {
        Group group = new Group();
        group.setGroupName(groupDTO.getGroupName());
        return group;
    }

    private void updateGroupFromDTO(Group group, GroupDTO groupDTO) {
        group.setGroupName(groupDTO.getGroupName());
    }
}
