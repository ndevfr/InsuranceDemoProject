package fr.ndev.insurance.controller.UserManage;

import fr.ndev.insurance.dto.UserDTOwithId;
import fr.ndev.insurance.enums.Role;
import fr.ndev.insurance.exception.ExceptionResponse;
import fr.ndev.insurance.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RolesAllowed("ADMIN, AGENT")
@RequestMapping("/api/users")
@Tag(name = "User edition (agent/admin)", description = "Endpoints for user management")
public class UserManageController {

    private final UserService userService;

    @Autowired
    UserManageController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users", description = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list of all users", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "[{\"user1\"}, {\"user2\"}, {\"user3\"}]"))),
    })
    @GetMapping("/")
    public List<UserDTOwithId> getAllUsers() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Change role for user", description = "Change role for user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role updated successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Role updated successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "409", description = "Role is already set to CLIENT", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"409 CONFLICT\",\"message\":\"Role is already set to CLIENT\"}"))),
            @ApiResponse(responseCode = "404", description = "Address not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"404 NOT_FOUND\",\"message\":\"User not found\"}"))),
    })
    @PutMapping("/{userId}/role/update")
    public ResponseEntity<?> changeRole(@RequestParam Role role, @PathVariable Long userId) {
        return userService.changeRole(userId, role);
    }

}