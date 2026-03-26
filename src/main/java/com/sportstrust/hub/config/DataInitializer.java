package com.sportstrust.hub.config;

import com.sportstrust.hub.entity.User;
import com.sportstrust.hub.entity.enums.Role;
import com.sportstrust.hub.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // Check if admin user exists (using email 'admin' as requested)
            if (!userRepository.existsByEmail("admin@gmail.com")) {
                User adminUser = User.builder()
                        .name("Administrator")
                        .email("admin@gmail.com")
                        .password(passwordEncoder.encode("admin123"))
                        .role(Role.ADMIN)
                        .build();
                
                userRepository.save(adminUser);
                System.out.println("Admin user initialized with email: 'admin@gmail.com' and password: 'admin123'");
            }
        };
    }
}
