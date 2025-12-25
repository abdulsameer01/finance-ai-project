package com.koushik.expansetracker.controller.security;

import com.koushik.expansetracker.dto.*;
import com.koushik.expansetracker.service.security.ForgotPasswordService;
import com.koushik.expansetracker.service.security.LoginService;
import com.koushik.expansetracker.service.security.SignupService;
import com.koushik.expansetracker.service.security.UserLoginResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final LoginService loginService;
    private final SignupService signupService;
    private final ForgotPasswordService forgotPasswordService;

    public AuthController(
            LoginService loginService,
            SignupService signupService,
            ForgotPasswordService forgotPasswordService
    ) {
        this.loginService = loginService;
        this.signupService = signupService;
        this.forgotPasswordService = forgotPasswordService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(
                loginService.login(request.getEmail(), request.getPassword())
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(
            @Valid @RequestBody SignupRequest request
    ) {
        return ResponseEntity.ok(signupService.signup(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(
            @RequestBody ForgotPasswordRequest request
    ) {
        String token = forgotPasswordService.generateResetToken(request);
        return ResponseEntity.ok("Reset link token: " + token);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
            @RequestBody ResetPasswordRequest request
    ) {
        return ResponseEntity.ok(forgotPasswordService.resetPassword(request));
    }
}
