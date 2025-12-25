package com.koushik.expansetracker.repository.security;

import com.koushik.expansetracker.entity.security.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

}
