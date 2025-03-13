package fr.ndev.insurance.controller.User;

import fr.ndev.insurance.dto.UserDTOwithId;
import fr.ndev.insurance.enums.Role;
import fr.ndev.insurance.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RolesAllowed("ADMIN, AGENT")
@RequestMapping("/api/agent/users")
@Tag(name = "agent_users", description = "Endpoints for user management")
public class UserRoleManageController {

    private final UserService userService;

    @Autowired
    UserRoleManageController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users", description = "Get all users")
    @GetMapping("/")
    public List<UserDTOwithId> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Change role for user", description = "Change role for user")
    @PutMapping("/{userId}/role")
    public ResponseEntity<?> changeRole(@RequestParam Role role, @PathVariable Long userId) {
        return userService.changeRole(userId, role);
    }

}