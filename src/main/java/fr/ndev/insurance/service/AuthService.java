package fr.ndev.insurance.service;

import fr.ndev.insurance.dto.*;
import fr.ndev.insurance.enums.Role;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.repository.UserRepository;
import fr.ndev.insurance.security.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final AccessTokenCookieService accessTokenCookieService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    AuthService(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, UserRepository userRepository, AccessTokenCookieService accessTokenCookieService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.accessTokenCookieService = accessTokenCookieService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Transactional
    public ResponseEntity<?> register(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new JsonResponse(HttpStatus.CONFLICT, "Email %s already exists".formatted(userDTO.getEmail())));
        }
        System.out.println(userDTO);
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = userDTO.toUser();
        if(userRepository.count() == 0) {
            user.setRole(Role.ADMIN);
        }
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(new JsonResponse(HttpStatus.CREATED, "User %s created successfully".formatted(user.getEmail())));
    }

    public ResponseEntity<?> login(LoginRequest loginRequest, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
            final String accessToken = jwtUtil.generateToken(userDetails.getUsername());
            final String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

            Instant expiresAt = jwtUtil.extractExpiration(accessToken).toInstant();
            User user = userRepository.findByEmail(loginRequest.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Cookie accessCookie = accessTokenCookieService.createAccessTokenCookie(accessToken);
            Cookie refreshCookie = accessTokenCookieService.createRefreshTokenCookie(refreshToken);

            response.addCookie(accessCookie);
            response.addCookie(refreshCookie);

            return ResponseEntity.ok(new AuthResponse(user, expiresAt));

        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponse(false));
        }
    }

    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Optional<Cookie> accessCookie = accessTokenCookieService.getAccessTokenCookie(request);
        Optional<Cookie> refreshCookie = accessTokenCookieService.getRefreshTokenCookie(request);

        if (refreshCookie.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No valid token found");
        }

        String refreshToken = refreshCookie.get().getValue();
        try {
            String username = jwtUtil.extractUsername(refreshToken);
            if (username != null && jwtUtil.validateToken(refreshToken, username)) {
                String newAccessToken = jwtUtil.generateToken(username);
                String newRefreshToken = jwtUtil.generateRefreshToken(username);

                Cookie newAccessCookie = accessTokenCookieService.createAccessTokenCookie(newAccessToken);
                Cookie newRefreshCookie = accessTokenCookieService.createRefreshTokenCookie(newRefreshToken);

                response.addCookie(newAccessCookie);
                response.addCookie(newRefreshCookie);

                return ResponseEntity.ok().body(Map.of("message", "Tokens refreshed successfully"));
            }
        } catch (Exception e) {
            logger.error("Error refreshing token", e);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid refresh token");
    }

    public ResponseEntity<?> logout(HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        Cookie accessCookie = accessTokenCookieService.createBlankAccessTokenCookie();
        response.addCookie(accessCookie);

        Cookie refreshCookie = accessTokenCookieService.createBlankRefreshTokenCookie();
        response.addCookie(refreshCookie);

        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponse(HttpStatus.OK, "Logged out successfully"));
    }

    public ResponseEntity<?> getInfos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        return ResponseEntity.ok(UserDTO.of(user));
    }

}
