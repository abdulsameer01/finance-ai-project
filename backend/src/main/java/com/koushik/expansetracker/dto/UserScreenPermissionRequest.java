package com.koushik.expansetracker.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserScreenPermissionRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Screen ID is required")
    private Long screenId;

    private boolean canView;
    private boolean canEdit;
    private boolean canDelete;
}
