package com.koushik.expansetracker.service.security;

import com.koushik.expansetracker.dto.UserPermissionSummary;
import com.koushik.expansetracker.dto.UserScreenResponse;
import com.koushik.expansetracker.entity.security.*;
import com.koushik.expansetracker.repository.security.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PermissionSummaryService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleScreenPermissionRepository roleScreenPermissionRepository;
    private final UserScreenPermissionRepository userScreenPermissionRepository;
    private final RoleRepository roleRepository;
    private final ScreenRepository screenRepository;

    public PermissionSummaryService(UserRepository userRepository, UserRoleRepository userRoleRepository, RoleScreenPermissionRepository roleScreenPermissionRepository, UserScreenPermissionRepository userScreenPermissionRepository, RoleRepository roleRepository, ScreenRepository screenRepository) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleScreenPermissionRepository = roleScreenPermissionRepository;
        this.userScreenPermissionRepository = userScreenPermissionRepository;
        this.roleRepository = roleRepository;
        this.screenRepository = screenRepository;
    }

    public UserPermissionSummary getUserPermissions(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1️⃣ Get all roles of the user
        List<Long> roleIds = userRoleRepository.findByUserId(userId)
                .stream().map(UserRole::getRoleId)
                .collect(Collectors.toList());

        // 2️⃣ Collect role-based screen permissions
        Map<Long, UserScreenResponse> finalPermissions = new HashMap<>();

        for (Long roleId : roleIds) {
            List<RoleScreenPermission> perms = roleScreenPermissionRepository.findByRoleId(roleId);

            for (RoleScreenPermission p : perms) {
                Screen screen = screenRepository.findById(p.getScreenId()).orElse(null);
                if (screen == null) continue;

                finalPermissions.put(screen.getScreenId(),
                        UserScreenResponse.builder()
                                .screenId(screen.getScreenId())
                                .screenName(screen.getScreenName())
                                .screenRoute(screen.getScreenRoute())
                                .canView(p.isCanView())
                                .canEdit(p.isCanEdit())
                                .canDelete(p.isCanDelete())
                                .build()
                );
            }
        }

        // 3️⃣ Apply user-specific overrides
        List<UserScreenPermission> overrides = userScreenPermissionRepository.findByUserId(userId);

        for (UserScreenPermission o : overrides) {
            Screen screen = screenRepository.findById(o.getScreenId()).orElse(null);
            if (screen == null) continue;

            finalPermissions.put(screen.getScreenId(),
                    UserScreenResponse.builder()
                            .screenId(screen.getScreenId())
                            .screenName(screen.getScreenName())
                            .screenRoute(screen.getScreenRoute())
                            .canView(o.isCanView())
                            .canEdit(o.isCanEdit())
                            .canDelete(o.isCanDelete())
                            .build()
            );
        }

        return UserPermissionSummary.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .screens(new ArrayList<>(finalPermissions.values()))
                .build();
    }
}
