package com.noc.smartnoc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Disable CSRF for REST API
            .cors(Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/incidencias/**").permitAll() // Permitir lectura pública para dashboard
                .requestMatchers(HttpMethod.GET, "/api/v1/equipos/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/v1/tecnicos/**").permitAll()
                .anyRequest().authenticated() // Proteger facturación, POST, PATCH, DELETE
            )
            .httpBasic(Customizer.withDefaults()); // Evidencia real de seguridad Basic Auth

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
            .username("admin")
            .password("{noop}admin123")
            .roles("ADMIN")
            .build();
            
        UserDetails user = User.builder()
            .username("operator")
            .password("{noop}oper123")
            .roles("USER")
            .build();

        return new InMemoryUserDetailsManager(admin, user);
    }
}
