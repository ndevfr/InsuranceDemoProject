package fr.ndev.insurance.service;

import fr.ndev.insurance.dto.*;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.model.Vehicle;
import fr.ndev.insurance.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final UserService userService;
    private final InsurancePolicyService insuranceService;
    private final VehicleRepository vehicleRepository;

    @Autowired
    VehicleService(UserService userService, InsurancePolicyService insuranceService, VehicleRepository vehicleRepository) {
        this.userService = userService;
        this.insuranceService = insuranceService;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public ResponseEntity<?> addVehicle(VehicleDTO vehicleDTO, Long userId) {
        User user = userService.getUser(userId);
        Vehicle vehicle = vehicleDTO.toVehicle();
        vehicle.setUser(user);
        vehicleRepository.save(vehicle);
        return ResponseEntity.ok(VehicleDTO.of(vehicle));
    }

    @Transactional
    public ResponseEntity<?> deleteVehicle(int index, Long userId) {
        User user = userService.getUser(userId);
        Vehicle vehicle = getUserVehicleById(user, index);
        if(insuranceService.getInsuranceByVehicle(vehicle) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponse(HttpStatus.BAD_REQUEST, "Vehicle is linked to an insurance policy"));
        }
        vehicleRepository.delete(vehicle);
        return ResponseEntity.ok(new JsonResponse(HttpStatus.OK, "Vehicle deleted successfully"));
    }

    @Transactional
    public ResponseEntity<?> updateVehicle(int index, VehicleDTO vehicleDTO, Long userId) {
        User user = userService.getUser(userId);
        Vehicle vehicle = vehicleDTO.toVehicle();
        Vehicle updatedVehicle = getUserVehicleById(user, index);
        updatedVehicle.updateVehicle(vehicle);
        vehicleRepository.save(updatedVehicle);
        return ResponseEntity.ok(VehicleDTO.of(updatedVehicle));
    }

    public ResponseEntity<?> getVehicle(int index, Long userId) {
        User user = userService.getUser(userId);
        Vehicle vehicle = getUserVehicleById(user, index);
        return ResponseEntity.ok(VehicleDTO.of(vehicle));
    }

    public List<VehicleDTO> getUserVehicles(Long userId) {
        User user = userService.getUser(userId);
        return vehicleRepository.findByUser(user).stream()
                .map(vehicle -> {
                    VehicleDTO dto = VehicleDTO.of(vehicle);
                    dto.setId(null); // Suppression de la clé-valeur id
                    return dto;
                })
                .toList();
    }

    public Vehicle getUserVehicleById(User user, int index) {
        List<Vehicle> vehicles = vehicleRepository.findByUser(user);
        try{
            return vehicles.get(index - 1);
        } catch (IndexOutOfBoundsException e) {
            new JsonResponse(HttpStatus.NOT_FOUND, "Vehicle not found");
        }
        return null;
    }

}
