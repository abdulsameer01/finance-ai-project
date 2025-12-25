package com.koushik.expansetracker.controller.security;

import com.koushik.expansetracker.dto.RoleScreenPermissionRequest;
import com.koushik.expansetracker.entity.security.RoleScreenPermission;
import com.koushik.expansetracker.service.security.RoleScreenPermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role-screen-permissions")
public class RoleScreenPermissionController {

    private final RoleScreenPermissionService roleScreenPermissionService;

    public RoleScreenPermissionController(RoleScreenPermissionService roleScreenPermissionService) {
        this.roleScreenPermissionService = roleScreenPermissionService;
    }

    @PostMapping
    public ResponseEntity<RoleScreenPermission> assignPermission(@RequestBody RoleScreenPermissionRequest request) {
        RoleScreenPermission permission = roleScreenPermissionService.assignPermission(request);
        return ResponseEntity.ok(permission);
    }
}
