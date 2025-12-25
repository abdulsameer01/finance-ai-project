package com.koushik.expansetracker.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserScreenResponse {
    private Long screenId;
    private String screenName;
    private String screenRoute;
    private boolean canView;
    private boolean canEdit;
    private boolean canDelete;
}
