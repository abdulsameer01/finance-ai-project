package com.koushik.expansetracker.service.security;

import com.koushik.expansetracker.security.CustomUserDetails;
import com.koushik.expansetracker.security.CustomUserDetailsService;
import com.koushik.expansetracker.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;
    private final PermissionSummaryService permissionSummaryService;
    private final CustomUserDetailsService userDetailsService;

    public LoginService(
            AuthenticationManager authManager,
            JwtService jwtService,
            PermissionSummaryService permissionSummaryService,
            CustomUserDetailsService userDetailsService
    ) {
        this.authManager = authManager;
        this.jwtService = jwtService;
        this.permissionSummaryService = permissionSummaryService;
        this.userDetailsService = userDetailsService;
    }

    public UserLoginResponse login(String email, String password) {

        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
        } catch (BadCredentialsException ex) {
            throw new AuthenticationCredentialsNotFoundException(
                    "Invalid email or password"
            );
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String token = jwtService.generateToken(email);

        var summary = permissionSummaryService.getUserPermissions(
                ((CustomUserDetails) userDetails).getUser().getUserId()
        );

        return new UserLoginResponse(token, summary);
    }
}
