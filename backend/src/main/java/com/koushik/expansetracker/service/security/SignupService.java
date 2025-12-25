package com.koushik.expansetracker.service.security;

import com.koushik.expansetracker.config.AccountSeeder;
import com.koushik.expansetracker.config.CategorySeeder;
import com.koushik.expansetracker.dto.SignupRequest;
import com.koushik.expansetracker.dto.SignupResponse;
import com.koushik.expansetracker.entity.security.Role;
import com.koushik.expansetracker.entity.security.User;
import com.koushik.expansetracker.entity.security.UserRole;
import com.koushik.expansetracker.repository.security.RoleRepository;
import com.koushik.expansetracker.repository.security.UserRepository;
import com.koushik.expansetracker.repository.security.UserRoleRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SignupService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountSeeder accountSeeder;
    private final CategorySeeder categorySeeder;

    public SignupService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder,
            AccountSeeder accountSeeder,
            CategorySeeder categorySeeder
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountSeeder = accountSeeder;
        this.categorySeeder = categorySeeder;
    }

    @Transactional
    public SignupResponse signup(SignupRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // ✅ 1️⃣ CREATE ACTIVE USER (🔥 FIX HERE)
        User user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isActive(true) // 🔥 THIS LINE FIXES LOGIN
                .build();

        User savedUser = userRepository.save(user);

        // ✅ 2️⃣ ASSIGN ROLE_USER
        Role defaultRole = roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("ROLE_USER not found"));

        userRoleRepository.save(
                UserRole.builder()
                        .userId(savedUser.getUserId())
                        .roleId(defaultRole.getRoleId())
                        .build()
        );

        // ✅ 3️⃣ SEED DEFAULT DATA
        accountSeeder.seedDefaultAccounts(savedUser.getUserId());
        categorySeeder.seedDefaultCategories(savedUser.getUserId());

        return new SignupResponse("Signup successful! Please login.");
    }
}
