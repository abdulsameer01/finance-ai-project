package com.koushik.expansetracker.controller.security;

import com.koushik.expansetracker.dto.CreateRoleRequest;
import com.koushik.expansetracker.entity.security.Role;
import com.koushik.expansetracker.service.security.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody CreateRoleRequest request) {
        Role savedRole = roleService.createRole(request);
        return ResponseEntity.ok(savedRole);
    }
}
