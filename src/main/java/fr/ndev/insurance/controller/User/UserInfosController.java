package fr.ndev.insurance.controller.User;

import fr.ndev.insurance.dto.PasswordRequest;
import fr.ndev.insurance.dto.ProfileRequest;
import fr.ndev.insurance.exception.ExceptionResponse;
import fr.ndev.insurance.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Tag(name = "users", description = "Endpoints for user profile")
public class UserInfosController {

    private final UserService userService;

    @Autowired
    UserInfosController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Update user informations", description = "Update informations in user profile")
    @PutMapping("/user")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileRequest profileRequest) {
        return userService.updateProfile(profileRequest, null);
    }

    @Operation(summary = "Update password", description = "Update password in user profile")
    @PutMapping("/user/password")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody PasswordRequest passwordRequest) {
        return userService.updatePassword(passwordRequest, null);
    }

    @Operation(summary = "Get user informations", description = "Get user informations")
    @GetMapping("/user")
    public ResponseEntity<?> getUserInfos() {
        return userService.getProfile(null);
    }

}
