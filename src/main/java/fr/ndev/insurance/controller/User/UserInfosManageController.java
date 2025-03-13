package fr.ndev.insurance.controller.User;

import fr.ndev.insurance.dto.ProfileRequest;
import fr.ndev.insurance.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/agent/users")
@Tag(name = "agent_users", description = "Endpoints for user management")
public class UserInfosManageController {

    private final UserService userService;

    @Autowired
    UserInfosManageController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Update user informations", description = "Update informations in user profile")
    @PutMapping("/{userId}")
    public ResponseEntity<?> updateProfile(@RequestBody ProfileRequest profileRequest, @PathVariable Long userId) {
        return userService.updateProfile(profileRequest, userId);
    }

    @Operation(summary = "Get user informations", description = "Get user informations")
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfos(@PathVariable Long userId) {
        return userService.getProfile(userId);
    }

}
