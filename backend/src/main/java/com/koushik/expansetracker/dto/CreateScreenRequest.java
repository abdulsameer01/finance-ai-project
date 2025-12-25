package com.koushik.expansetracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateScreenRequest {

    @NotBlank(message = "Screen name is required")
    @Size(max = 100, message = "Screen name cannot exceed 100 characters")
    private String screenName;

    @NotBlank(message = "Screen route is required")
    @Size(max = 200, message = "Screen route cannot exceed 200 characters")
    private String screenRoute;

    private boolean isMenu;
}
