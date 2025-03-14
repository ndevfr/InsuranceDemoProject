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
@RequestMapping("/api/user")
@Tag(name = "vehicles", description = "Endpoints for vehicles management (user)")
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @Operation(summary = "Get user vehicles", description = "Get user vehicles")
    @GetMapping("/vehicles")
    public List<VehicleDTO> getUserVechicles() {
        return vehicleService.getUserVehicles(null);
    }

    @Operation(summary = "Add vehicle", description = "Add vehicle to user profile")
    @PostMapping("/vehicles")
    public ResponseEntity<?> addVehicle(@RequestBody @Valid VehicleDTO vehicleRequest) {
        return vehicleService.addVehicle(vehicleRequest, null);
    }

    @Operation(summary = "Update vehicle", description = "Update vehicle in user profile")
    @PutMapping("/vehicles/{id}")
    public ResponseEntity<?> updateVehicle(@PathVariable int id, @RequestBody @Valid VehicleDTO vehicleRequest) {
        return vehicleService.updateVehicle(id, vehicleRequest, null);
    }

    @Operation(summary = "Get vehicle", description = "Get vehicle in user profile")
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<?> getVehicle(@PathVariable int id) {
        return vehicleService.getVehicle(id, null);
    }

    @Operation(summary = "Delete vehicle", description = "Delete vehicle from user profile")
    @DeleteMapping("/vehicles/{id}")
    public ResponseEntity<?> deleteVehicle(@PathVariable int id) {
        return vehicleService.deleteVehicle(id, null);
    }

}