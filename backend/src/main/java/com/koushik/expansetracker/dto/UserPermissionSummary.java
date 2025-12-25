package com.koushik.expansetracker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class UserPermissionSummary {
    private Long userId;
    private String username;
    private List<UserScreenResponse> screens;
}
