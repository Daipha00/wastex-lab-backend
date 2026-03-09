package com.suza.wasteX.user;

import com.suza.wasteX.user.Role;
import com.suza.wasteX.user.User;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class AdminConfig {

    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner createAdmin(UserRepo userRepository) {
        return args -> {
            if (!userRepository.existsByEmail("admin@wastex.suza.ac.tz")) {
                User admin = User.builder()
                        .firstName("Ally")
                        .lastName("Juma")
                        .email("admin@wastex.suza.ac.tz")
                        .password(passwordEncoder.encode("Admin@123"))
                        .role(Role.ADMIN)
                        .build();

                userRepository.save(admin);
                System.out.println("Admin user created");
            }
        };
    }
}
