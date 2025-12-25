package com.koushik.expansetracker.service.security;

import com.koushik.expansetracker.dto.UserScreenPermissionRequest;
import com.koushik.expansetracker.entity.security.UserScreenPermission;
import com.koushik.expansetracker.repository.security.UserScreenPermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class UserScreenPermissionService {

    private final UserScreenPermissionRepository userScreenPermissionRepository;

    public UserScreenPermissionService(UserScreenPermissionRepository userScreenPermissionRepository) {
        this.userScreenPermissionRepository = userScreenPermissionRepository;
    }

    public UserScreenPermission assignUserPermission(UserScreenPermissionRequest request) {

        UserScreenPermission permission = UserScreenPermission.builder()
                .userId(request.getUserId())
                .screenId(request.getScreenId())
                .canView(request.isCanView())
                .canEdit(request.isCanEdit())
                .canDelete(request.isCanDelete())
                .build();

        return userScreenPermissionRepository.save(permission);
    }
}
