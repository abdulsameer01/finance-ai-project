package com.koushik.expansetracker.entity.security;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_screen_permissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"roleId", "screenId"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RoleScreenPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleScreenPermId;

    private Long roleId;
    private Long screenId;

    private boolean canView;
    private boolean canEdit;
    private boolean canDelete;
}

