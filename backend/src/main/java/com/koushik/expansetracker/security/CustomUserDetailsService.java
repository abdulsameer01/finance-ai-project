package com.koushik.expansetracker.security;

import com.koushik.expansetracker.entity.security.Role;
import com.koushik.expansetracker.entity.security.User;
import com.koushik.expansetracker.entity.security.UserRole;
import com.koushik.expansetracker.repository.security.RoleRepository;
import com.koushik.expansetracker.repository.security.UserRepository;
import com.koushik.expansetracker.repository.security.UserRoleRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    public CustomUserDetailsService(
            UserRepository userRepository,
            UserRoleRepository userRoleRepository,
            RoleRepository roleRepository
    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        // 1️⃣ Load user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found with email: " + email
                        )
                );

        // 2️⃣ Load role IDs from user_roles
        List<Long> roleIds = userRoleRepository
                .findByUserId(user.getUserId())
                .stream()
                .map(UserRole::getRoleId)
                .toList();

        // 3️⃣ Load roles
        List<Role> roles = roleRepository.findAllById(roleIds);

        // 4️⃣ Convert to authorities
        List<GrantedAuthority> authorities =
                roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                        .collect(Collectors.toList());

        // 5️⃣ Return UserDetails
        return new CustomUserDetails(user, authorities);
    }
}
