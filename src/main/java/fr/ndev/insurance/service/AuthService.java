package fr.ndev.insurance.service;

import fr.ndev.insurance.dto.*;
import fr.ndev.insurance.enums.Role;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.repository.UserRepository;
import fr.ndev.insurance.security.JwtUtil;
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

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

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

    public ResponseEntity<?> login(LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new JsonResponse(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new JsonResponse(HttpStatus.OK, jwt));
    }

    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        return ResponseEntity.status(HttpStatus.OK).body(new JsonResponse(HttpStatus.OK, "Logged out successfully"));
    }

    public ResponseEntity<?> getInfos() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByEmail(userDetails.getUsername());
        return ResponseEntity.ok(UserDTO.of(user));
    }

}
