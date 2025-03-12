package fr.ndev.insurance.controller.Vehicle;

import fr.ndev.insurance.dto.VehicleDTO;
import fr.ndev.insurance.exception.ExceptionResponse;
import fr.ndev.insurance.service.VehicleService;
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

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Vehicle management (agent/admin)", description = "Endpoints for user management")
public class VehiculeManageController {

    private final VehicleService vehicleService;

    @Autowired
    VehiculeManageController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(summary = "Get user vehicles", description = "Get user vehicles")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list of user vehicles", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "[{\"vehicle1\"}, {\"vehicle2\"}]"))),
    })
    @GetMapping("/{userId}/vehicles")
    public List<VehicleDTO> getUserVechicles(@PathVariable Long userId) {
        return vehicleService.getUserVehicles(userId);
    }

    @Operation(summary = "Add vehicle", description = "Add vehicle to user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle added successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Vehicle added successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"400 BAD_REQUEST\",\"errors\": [\"brand must not be blank\"]}"))),
    })
    @PostMapping("/{userId}/vehicle/add")
    public ResponseEntity<?> addVehicle(@RequestBody @Valid VehicleDTO vehicleRequest, @PathVariable Long userId) {
        return vehicleService.addVehicle(vehicleRequest, userId);
    }

    @Operation(summary = "Update vehicle", description = "Update vehicle in user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle updated successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Vehicle updated successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"400 BAD_REQUEST\",\"errors\": [\"brand must not be blank\"]}"))),
            @ApiResponse(responseCode = "404", description = "Address not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"404 NOT_FOUND\",\"message\":\"Vehicle not found\"}"))),
    })
    @PutMapping("/{userId}/vehicle/update/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable int id, @RequestBody @Valid VehicleDTO vehicleRequest, @PathVariable Long userId) {
        return vehicleService.updateVehicle(id, vehicleRequest, userId);
    }

    @Operation(summary = "Delete vehicle", description = "Delete vehicle from user profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Vehicle deleted successfully", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"200 OK\",\"message\":\"Vehicle deleted successfully\"}"))),
            @ApiResponse(responseCode = "401", description = "Invalid credentials", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"401 UNAUTHORIZED\",\"message\":\"Unauthorized endpoint\"}"))),
            @ApiResponse(responseCode = "409", description = "Vehicle deletion is impossible", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"409 CONFLICT\",\"message\":\"Cannot delete the only address\"}"))),
            @ApiResponse(responseCode = "404", description = "Address not found", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExceptionResponse.class,
                            example = "{\"status\": \"404 NOT_FOUND\",\"message\":\"Vehicle not found\"}"))),
    })
    @DeleteMapping("/{userId}/vehicle/delete/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable int id, @PathVariable Long userId) {
        return vehicleService.deleteVehicle(id, userId);
    }

}