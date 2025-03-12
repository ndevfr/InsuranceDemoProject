package fr.ndev.insurance.controller.User;

import fr.ndev.insurance.dto.PhoneDTO;
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
@RequestMapping("/api/user")
@Tag(name = "User edition", description = "Endpoints for user profile")
public class UserPhoneController {

    private final UserService userService;

    @Autowired
    UserPhoneController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Add phone number", description = "Add phone number to user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone number added successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Phone number added successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Invalid email or password\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"400 BAD_REQUEST\",\"errors\": [\"phoneNumber must not be blank\"]}"))),
    })
    @PostMapping("/phone/add")
    public ResponseEntity<?> addPhone(@RequestBody @Valid PhoneDTO phoneRequest) {
        return userService.addPhone(phoneRequest, null);
    }

    @Operation(summary = "Update phone number", description = "Update phone number in user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone number updated successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Phone number updated successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"400 BAD_REQUEST\",\"errors\": [\"phoneNumber must not be blank\"]}"))),
            @ApiResponse(responseCode = "404", description = "Address not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"404 NOT_FOUND\",\"message\":\"Phone number not found\"}"))),
    })
    @PutMapping("/phone/update/{id}")
    public ResponseEntity<?> updatePhone(@PathVariable int id, @RequestBody @Valid PhoneDTO phoneRequest) {
        return userService.updatePhone(id, phoneRequest, null);
    }

    @Operation(summary = "Set phone number as main", description = "Set phone number as main in user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone number chosen as main successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Phone number chosen as main successfully...\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "409", description = "Phone number is already the main phone number", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"409 CONFLICT\",\"message\":\"Phone number is already the main phone number\"}"))),
            @ApiResponse(responseCode = "404", description = "Address not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"404 NOT_FOUND\",\"message\":\"Phone number not found\"}"))),
    })
    @PutMapping("/phone/main/{id}")
    public ResponseEntity<?> choosePhoneAsMain(@PathVariable int id) {
        return userService.choosePhoneAsMain(id, null);
    }

    @Operation(summary = "Delete phone number", description = "Delete phone number from user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Phone number deleted successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Phone number deleted successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "409", description = "Phone number deletion is impossible", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"409 CONFLICT\",\"message\":\"Cannot delete the main phone number\"}"))),
            @ApiResponse(responseCode = "404", description = "Phone number not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"404 NOT_FOUND\",\"message\":\"Phone number not found\"}"))),
    })
    @DeleteMapping("/phone/delete/{id}")
    public ResponseEntity<?> deletePhone(@PathVariable int id) {
        return userService.deletePhone(id, null);
    }

}
