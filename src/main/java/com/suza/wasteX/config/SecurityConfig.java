package com.suza.wasteX.config;

import com.suza.wasteX.user.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
               .cors(cors -> {})
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/users/register", "/api/users/login",
                                "/api/users/count","/api/v1/projects/","/api/v1/members/","/api/v1/members/",
                                "/api/v1/members/{activityId}","/api/v1/members/activity/{activityId}","/api/v1/members/**",
                                "api/v1/member/counts/","api/v1/types/","api/v1/member/counts/{activityId}",
                                 "api/v1/sponsor","api/v1/sponsor/{sponsorId}",
                                "/api/v1/projects/{projectId}/sponsors",
                                "/api/v1/projects/",
                                "api/v1/member/counts/**",
                                 "/api/v1/applicants/**",
                                 "/api/v1/members/**",
                                 "/api/v1/members/{memberId}/status",
                                "/api/v1/projects/**",
                                "/api/v1/projects/count/",
                                "api/v1/sponsor",
                                "api/v1/sponsor/{sponsorId}",
                                "api/v1/types/counts",
                                "api/v1/types/",
                                "api/v1/types/total-count",
                                "api/v1/types/{typeId}",
                                "/api/v1/visitors/",
                                "/api/v1/features/**",
                                "/api/v1/carousel-items/**",
                                "/api/v1/testimonials/**",
                                "/api/v1/carousel-items",
                                "api/v1/testimonials/{id}",
                                "api/v1/activities/","api/v1/activities/{projectId}",
                                "api/v1/activities/{activityId}",

                                "api/v1/activities/{projectId}/{activityId}","/api/v1/projects/{id}",
                                "api/v1/activities/status/{id}","api/v1/activities/{activityId}/sponsors",
                                "api/v1/activities/{projectId}/sponsors","api/v1/activities/project/{projectId}",
                                "/api/partners","/api/partners/images",
                                "/api/v1/galleries/**",
                                "/api/v1/events/**",
                                "/api/v1/galleries",
                                "/api/partners/{id}", "/api/partners/{id}/image",
                                "/api/v1/projects/{projectId}/activities").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }
}
