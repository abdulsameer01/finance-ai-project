package com.koushik.expansetracker.service.security;

import com.koushik.expansetracker.dto.UserPermissionSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponse {
    private String token;
    private UserPermissionSummary permissions;
}
