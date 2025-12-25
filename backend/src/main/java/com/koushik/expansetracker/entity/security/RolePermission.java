package com.koushik.expansetracker.entity.security;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "role_permissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"roleId", "permissionId"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rolePermissionId;

    private Long roleId;
    private Long permissionId;
}

