package fr.ndev.insurance.security;

import fr.ndev.insurance.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.userdetails.UserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(handing -> handing
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint) // Handles auth error
                )
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)) // for Postman, the H2 console
                .authorizeHttpRequests(requests -> requests // manage access
                    .requestMatchers(HttpMethod.POST, "/api/auth/register", "/api/auth/login").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/auth/me").hasAnyRole("CLIENT", "AGENT", "ADMIN")
                    .requestMatchers("/user/**").hasAnyRole("CLIENT", "AGENT", "ADMIN")
                    .requestMatchers("/users/**").hasAnyRole("AGENT", "ADMIN")
                    .requestMatchers("users/*/role/update").hasRole("ADMIN")
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/client/**").hasRole("CLIENT")
                    .requestMatchers("/agent/**").hasRole("AGENT")
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api-docs/**").permitAll()
                    .requestMatchers("/swagger-ui.html").permitAll()
                    .requestMatchers("/swagger-ui/**").permitAll()
                    .requestMatchers("/swagger-resources/**").permitAll()
                    .requestMatchers("/webjars/**").permitAll()
                    .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
        return authManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}