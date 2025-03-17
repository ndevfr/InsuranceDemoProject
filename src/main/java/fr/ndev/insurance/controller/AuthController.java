package fr.ndev.insurance.controller;

import fr.ndev.insurance.dto.LoginRequest;
import fr.ndev.insurance.dto.UserDTO;
import fr.ndev.insurance.exception.ExceptionResponse;
import fr.ndev.insurance.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "auth", description = "Endpoints for user authentication")
public class AuthController {

    private final AuthService userService;

    @Autowired
    AuthController(AuthService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Register a new user", description = "Creates a new user account")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserDTO userDTO) {
        return userService.register(userDTO);
    }

    @Operation(summary = "Authenticate user", description = "Validates credentials and returns JWT token")
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody @Valid LoginRequest loginRequest, HttpServletResponse response) {
        return userService.login(loginRequest, response);
    }

    @Operation(summary = "Refresh token", description = "Refresh token")
    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        return userService.refreshToken(request, response);
    }

    @Operation(summary = "Logout from service", description = "Logout from service and invalidate token")
    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        return userService.logout(response);
    }

    @Operation(summary = "Get informations", description = "Get user informations")
    @GetMapping("/me")
    public ResponseEntity<?> infosUser() {
        return userService.getInfos();
    }
}