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
@RequestMapping("/api/agent/users")
@Tag(name = "agent_vehicles", description = "Endpoints for vehicles management (adgent/admin)")
public class VehicleManageController {

    private final VehicleService vehicleService;

    @Autowired
    VehicleManageController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(summary = "Get user vehicles", description = "Get user vehicles")
    @GetMapping("/{userId}/vehicles")
    public List<VehicleDTO> getUserVechicles(@PathVariable Long userId) {
        return vehicleService.getUserVehicles(userId);
    }

    @Operation(summary = "Add vehicle", description = "Add vehicle to user profile")
    @PostMapping("/{userId}/vehicles")
    public ResponseEntity<?> addVehicle(@RequestBody @Valid VehicleDTO vehicleRequest, @PathVariable Long userId) {
        return vehicleService.addVehicle(vehicleRequest, userId);
    }

    @Operation(summary = "Update vehicle", description = "Update vehicle in user profile")
    @PutMapping("/{userId}/vehicles/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable int id, @RequestBody @Valid VehicleDTO vehicleRequest, @PathVariable Long userId) {
        return vehicleService.updateVehicle(id, vehicleRequest, userId);
    }

    @Operation(summary = "Get vehicle", description = "Get vehicle in user profile")
    @GetMapping("/{userId}/vehicles/{id}")
    public ResponseEntity<?> getVehicle(@PathVariable int id,  @PathVariable Long userId) {
        return vehicleService.getVehicle(id, userId);
    }

    @Operation(summary = "Delete vehicle", description = "Delete vehicle from user profile")
    @DeleteMapping("/{userId}/vehicles/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable int id, @PathVariable Long userId) {
        return vehicleService.deleteVehicle(id, userId);
    }

}