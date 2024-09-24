package com.mobisoft.taskmanagement.dto;
import com.mobisoft.taskmanagement.entity.Role;

import lombok.Data;

@Data
public class UserRoleDTO {
    private Long userId;
    private Role role;
}
