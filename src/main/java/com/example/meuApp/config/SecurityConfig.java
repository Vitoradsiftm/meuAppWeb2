package com.example.meuApp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService uds;

    @Autowired
    private BCryptPasswordEncoder encoder;

    // Cadeia 1: cuida de todas as requisições /api/**
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/api/**")
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/public/**").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .httpBasic(basic -> {})
            .authenticationProvider(authenticationProvider(uds, encoder));

        return http.build();
    }

    // Cadeia 2: cuida das páginas web normais (formulários, rotas /carro, etc.)
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(requests -> requests
                .requestMatchers("/home", "/register", "/saveUser", "/error", "/swagger-ui/**", "/v3/api-docs*/**").permitAll()

                // qualquer usuário logado pode visualizar
                .requestMatchers("/carro").authenticated()

                // somente ADMIN pode criar/salvar/excluir
                .requestMatchers("/carro/**").hasAuthority("Admin")

                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .defaultSuccessUrl("/", true)
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
            )
            .exceptionHandling(handling -> handling
                .accessDeniedPage("/accessDenied")
            )
            .authenticationProvider(authenticationProvider(uds, encoder));

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider authProvider =
                new DaoAuthenticationProvider(userDetailsService);

        authProvider.setPasswordEncoder(passwordEncoder);

        return authProvider;
    }
}