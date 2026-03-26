package com.sportstrust.hub.service;

import com.sportstrust.hub.dto.auth.AuthResponse;
import com.sportstrust.hub.dto.auth.LoginRequest;
import com.sportstrust.hub.dto.auth.RegisterRequest;
import com.sportstrust.hub.entity.AthleteProfile;
import com.sportstrust.hub.entity.User;
import com.sportstrust.hub.entity.enums.Role;
import com.sportstrust.hub.entity.enums.VerificationStatus;
import com.sportstrust.hub.exception.CustomException;
import com.sportstrust.hub.repository.AthleteProfileRepository;
import com.sportstrust.hub.repository.UserRepository;
import com.sportstrust.hub.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AthleteProfileRepository athleteProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @SuppressWarnings("null")
@Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomException("Email is already in use", HttpStatus.BAD_REQUEST);
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        @SuppressWarnings("null")
        User savedUser = userRepository.save(user);

        // If user is an athlete, create a basic profile
        if (request.getRole() == Role.ATHLETE) {
            AthleteProfile profile = AthleteProfile.builder()
                    .user(savedUser)
                    .sport(request.getSport() != null ? request.getSport() : "Unknown")
                    .position(request.getPosition())
                    .experienceYears(request.getExperienceYears() != null ? request.getExperienceYears() : 0)
                    .verificationStatus(VerificationStatus.BASIC)
                    .credibilityScore(0.0)
                    .totalTournaments(0)
                    .totalWins(0)
                    .build();
            athleteProfileRepository.save(profile);
        }

        String jwtToken = jwtUtils.generateToken(savedUser);
        String refreshToken = jwtUtils.generateRefreshToken(savedUser);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userId(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("User not found", HttpStatus.NOT_FOUND));

        String jwtToken = jwtUtils.generateToken(user);
        String refreshToken = jwtUtils.generateRefreshToken(user);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
