package fr.ndev.insurance.controller.UserManage;

import fr.ndev.insurance.dto.AddressDTO;
import fr.ndev.insurance.exception.ExceptionResponse;
import fr.ndev.insurance.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User edition (agent/admin)", description = "Endpoints for user management")
public class UserManageAddressController {

    private final UserService userService;

    @Autowired
    UserManageAddressController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Add postal address", description = "Add postal address to user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address added successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Address added successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Invalid email or password\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"400 BAD_REQUEST\",\"errors\": [\"city must not be blank\"]}"))),
    })
    @PostMapping("/{userId}/address/add")
    public ResponseEntity<?> addAddress(@RequestBody @Valid AddressDTO addressRequest, @PathVariable Long userId) {
        return userService.addAddress(addressRequest, userId);
    }

    @Operation(summary = "Update postal address", description = "Update postal address in user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Address updated successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"400 BAD_REQUEST\",\"errors\": [\"city must not be blank\"]}"))),
            @ApiResponse(responseCode = "404", description = "Address not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"404 NOT_FOUND\",\"message\":\"Address not found\"}"))),
    })
    @PutMapping("/{userId}/address/update/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable int id, @RequestBody @Valid AddressDTO addressRequest, @PathVariable Long userId) {
        return userService.updateAddress(id, addressRequest, userId);
    }

    @Operation(summary = "Set postal address as main", description = "Set postal address as main in user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address chosen as main successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Address chosen as main successfully...\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "409", description = "Address is already the main address", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"409 CONFLICT\",\"message\":\"Address is already the main address\"}"))),
            @ApiResponse(responseCode = "404", description = "Address not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"404 NOT_FOUND\",\"message\":\"Address not found\"}"))),
    })
    @PutMapping("/{userId}/address/main/{id}")
    public ResponseEntity<?> chooseAddressAsMain(@PathVariable int id, @PathVariable Long userId) {
        return userService.chooseAddressAsMain(id, userId);
    }

    @Operation(summary = "Delete postal address", description = "Delete postal address from user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address deleted successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Address deleted successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "409", description = "Address deletion is impossible", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"409 CONFLICT\",\"message\":\"Cannot delete the main address\"}"))),
            @ApiResponse(responseCode = "404", description = "Address not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"404 NOT_FOUND\",\"message\":\"Address not found\"}"))),
    })
    @DeleteMapping("/{userId}/address/delete/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable int id, @PathVariable Long userId) {
        return userService.deleteAddress(id, userId);
    }

}
