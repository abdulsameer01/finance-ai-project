package com.koushik.expansetracker.service.security;

import com.koushik.expansetracker.dto.AssignRoleRequest;
import com.koushik.expansetracker.entity.security.UserRole;
import com.koushik.expansetracker.repository.security.UserRoleRepository;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository userRoleRepository) {
        this.userRoleRepository = userRoleRepository;
    }

    public UserRole assignRole(AssignRoleRequest request) {

        UserRole ur = UserRole.builder()
                .userId(request.getUserId())
                .roleId(request.getRoleId())
                .build();

        return userRoleRepository.save(ur);
    }
}
