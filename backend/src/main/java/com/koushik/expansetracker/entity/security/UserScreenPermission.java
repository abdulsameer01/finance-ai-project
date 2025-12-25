package com.koushik.expansetracker.entity.security;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_screen_permissions",
        uniqueConstraints = @UniqueConstraint(columnNames = {"userId", "screenId"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UserScreenPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uspId;

    private Long userId;
    private Long roleId;
    private Long screenId;

    private boolean canView;
    private boolean canEdit;
    private boolean canDelete;
}

