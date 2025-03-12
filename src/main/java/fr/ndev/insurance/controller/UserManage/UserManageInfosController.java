package fr.ndev.insurance.controller.UserManage;

import fr.ndev.insurance.exception.ExceptionResponse;
import fr.ndev.insurance.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User edition (agent/admin)", description = "Endpoints for user management")
public class UserManageInfosController {

    private final UserService userService;

    @Autowired
    UserManageInfosController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Update email", description = "Update email in user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email updated successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Email updated successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"400 BAD_REQUEST\",\"errors\": [\"email must not be blank\"]}"))),
    })
    @PutMapping("/{userId}/email/update")
    public ResponseEntity<?> updateEmail(@RequestParam @Email String email, @PathVariable Long userId) {
        return userService.updateEmail(email, userId);
    }

    @Operation(summary = "Update password", description = "Update password in user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Password updated successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"400 BAD_REQUEST\",\"errors\": [\"password must not be blank\"]}"))),
    })
    @PutMapping("/{userId}/password/update")
    public ResponseEntity<?> updatePassword(@RequestParam @NotBlank String password, @PathVariable Long userId) {
        return userService.updatePassword(password, userId);
    }

    @Operation(summary = "Update firstname", description = "Update firstname in user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Firstname updated successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Firstname updated successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"400 BAD_REQUEST\",\"errors\": [\"firstname must not be blank\"]}"))),
    })
    @PutMapping("/{userId}/firstname/update")
    public ResponseEntity<?> updateFirstname(@RequestParam @NotBlank String firstname, @PathVariable Long userId) {
        return userService.updateFirstname(firstname, userId);
    }

    @Operation(summary = "Update lastname", description = "Update lastname in user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lastname updated successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Lastname updated successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"400 BAD_REQUEST\",\"errors\": [\"lastname must not be blank\"]}"))),
    })
    @PutMapping("/{userId}/lastname/update")
    public ResponseEntity<?> updateLastname(@RequestParam @NotBlank String lastname, @PathVariable Long userId) {
        return userService.updateLastname(lastname, userId);
    }

}
