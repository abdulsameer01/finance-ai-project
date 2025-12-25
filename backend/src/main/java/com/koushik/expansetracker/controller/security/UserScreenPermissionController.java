package com.koushik.expansetracker.controller.security;

import com.koushik.expansetracker.dto.UserScreenPermissionRequest;
import com.koushik.expansetracker.entity.security.UserScreenPermission;
import com.koushik.expansetracker.service.security.UserScreenPermissionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-screen-permissions")
public class UserScreenPermissionController {

    private final UserScreenPermissionService userScreenPermissionService;

    public UserScreenPermissionController(UserScreenPermissionService userScreenPermissionService) {
        this.userScreenPermissionService = userScreenPermissionService;
    }

    @PostMapping
    public ResponseEntity<UserScreenPermission> assignPermission(@RequestBody UserScreenPermissionRequest request) {
        UserScreenPermission permission = userScreenPermissionService.assignUserPermission(request);
        return ResponseEntity.ok(permission);
    }
}
