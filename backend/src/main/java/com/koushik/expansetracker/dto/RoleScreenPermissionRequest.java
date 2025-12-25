package com.koushik.expansetracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleScreenPermissionRequest {

    @NotNull(message = "Role ID is required")
    private Long roleId;

    @NotNull(message = "Screen ID is required")
    private Long screenId;

    private boolean canView;
    private boolean canEdit;
    private boolean canDelete;
}
