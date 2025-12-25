package com.koushik.expansetracker.service.security;

import com.koushik.expansetracker.dto.ForgotPasswordRequest;
import com.koushik.expansetracker.dto.ResetPasswordRequest;
import com.koushik.expansetracker.entity.security.PasswordResetToken;
import com.koushik.expansetracker.entity.security.User;
import com.koushik.expansetracker.repository.security.PasswordResetTokenRepository;
import com.koushik.expansetracker.repository.security.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository tokenRepository;
    private final PasswordEncoder encoder;

    public ForgotPasswordService(UserRepository userRepository,
                                 PasswordResetTokenRepository tokenRepository,
                                 PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.encoder = encoder;
    }

    public String generateResetToken(ForgotPasswordRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Email not found"));

        String token = UUID.randomUUID().toString();

        PasswordResetToken prt = PasswordResetToken.builder()
                .userId(user.getUserId())
                .token(token)
                .expiryTime(Instant.now().plusSeconds(15 * 60)) // 15 minutes
                .build();

        tokenRepository.save(prt);

        return token; // frontend will convert this into a link
    }

    public String resetPassword(ResetPasswordRequest request) {

        PasswordResetToken prt = tokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (prt.isUsed()) {
            throw new RuntimeException("Token already used");
        }

        if (prt.getExpiryTime().isBefore(Instant.now())) {
            throw new RuntimeException("Token expired");
        }

        User user = userRepository.findById(prt.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setPassword(encoder.encode(request.getNewPassword()));
        userRepository.save(user);

        prt.setUsed(true);
        tokenRepository.save(prt);

        return "Password reset successfully";
    }
}
