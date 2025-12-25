package com.koushik.expansetracker.controller.security;

import com.koushik.expansetracker.dto.UserPermissionSummary;
import com.koushik.expansetracker.service.security.PermissionSummaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permissions")
public class PermissionSummaryController {

    private final PermissionSummaryService permissionSummaryService;

    public PermissionSummaryController(PermissionSummaryService permissionSummaryService) {
        this.permissionSummaryService = permissionSummaryService;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<UserPermissionSummary> getPermissions(@PathVariable Long userId) {
        return ResponseEntity.ok(permissionSummaryService.getUserPermissions(userId));
    }
}
