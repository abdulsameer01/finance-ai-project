package com.koushik.expansetracker.entity.security;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_roles",
        uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "roleId"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userRoleId;

    private Long userId;
    private Long roleId;
}

