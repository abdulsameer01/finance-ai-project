package com.koushik.expansetracker.service.security;

import com.koushik.expansetracker.dto.RoleScreenPermissionRequest;
import com.koushik.expansetracker.entity.security.RoleScreenPermission;
import com.koushik.expansetracker.repository.security.RoleScreenPermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleScreenPermissionService {

    private final RoleScreenPermissionRepository roleScreenPermissionRepository;

    public RoleScreenPermissionService(RoleScreenPermissionRepository roleScreenPermissionRepository) {
        this.roleScreenPermissionRepository = roleScreenPermissionRepository;
    }

    public RoleScreenPermission assignPermission(RoleScreenPermissionRequest request) {

        RoleScreenPermission permission = RoleScreenPermission.builder()
                .roleId(request.getRoleId())
                .screenId(request.getScreenId())
                .canView(request.isCanView())
                .canEdit(request.isCanEdit())
                .canDelete(request.isCanDelete())
                .build();

        return roleScreenPermissionRepository.save(permission);
    }
}
