package com.koushik.expansetracker.service.security;

import com.koushik.expansetracker.dto.CreateRoleRequest;
import com.koushik.expansetracker.entity.security.Role;
import com.koushik.expansetracker.repository.security.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role createRole(CreateRoleRequest request) {

        Role role = Role.builder()
                .roleName(request.getRoleName())
                .roleDescription(request.getRoleDescription())
                .build();

        return roleRepository.save(role);
    }
}
