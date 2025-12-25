package com.koushik.expansetracker.controller.security;

import com.koushik.expansetracker.dto.AssignRoleRequest;
import com.koushik.expansetracker.entity.security.UserRole;
import com.koushik.expansetracker.service.security.UserRoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-roles")
public class UserRoleController {

    private final UserRoleService userRoleService;

    public UserRoleController(UserRoleService userRoleService) {
        this.userRoleService = userRoleService;
    }

    @PostMapping
    public ResponseEntity<UserRole> assignRole(@RequestBody AssignRoleRequest request) {
        UserRole userRole = userRoleService.assignRole(request);
        return ResponseEntity.ok(userRole);
    }
}
