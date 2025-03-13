package fr.ndev.insurance.service;

import fr.ndev.insurance.dto.*;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.model.Vehicle;
import fr.ndev.insurance.repository.UserRepository;
import fr.ndev.insurance.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {

    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    @Autowired
    VehicleService(UserRepository userRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public ResponseEntity<?> addVehicle(VehicleDTO vehicleDTO, Long userId) {
        User user = getUser(userId);
        Vehicle vehicle = vehicleDTO.toVehicle();
        vehicle.setUser(user);
        vehicleRepository.save(vehicle);
        return ResponseEntity.ok(VehicleDTO.of(vehicle));
    }

    @Transactional
    public ResponseEntity<?> deleteVehicle(int index, Long userId) {
        User user = getUser(userId);
        index = index - 1;
        List<Vehicle> vehicles = vehicleRepository.findByUser(user);
        int vehicleCount = vehicles.size();
        if(index < 0 || index >= vehicleCount ) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, "Vehicle not found"));
        } else if(vehicleCount == 1) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new JsonResponse(HttpStatus.CONFLICT, "Cannot delete the only vehicle"));
        }
        Vehicle vehicle = vehicles.get(index);
        vehicleRepository.delete(vehicle);
        return ResponseEntity.ok(new JsonResponse(HttpStatus.OK, "Vehicle deleted successfully"));
    }

    @Transactional
    public ResponseEntity<?> updateVehicle(int index, VehicleDTO vehicleDTO, Long userId) {
        User user = getUser(userId);
        Vehicle vehicle = vehicleDTO.toVehicle();
        index = index - 1;
        List<Vehicle> vehicles = vehicleRepository.findByUser(user);
        int vehicleCount = vehicles.size();
        if(index < 0 || index >= vehicleCount) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, "Phone number not found"));
        }
        Vehicle updatedVehicle = vehicles.get(index);
        updatedVehicle.updateVehicle(vehicle);
        vehicleRepository.save(updatedVehicle);
        return ResponseEntity.ok(VehicleDTO.of(updatedVehicle));
    }

    public List<VehicleDTO> getUserVehicles(Long userId) {
        User user = getUser(userId);
        return vehicleRepository.findByUser(user).stream()
                .map(vehicle -> {
                    VehicleDTO dto = VehicleDTO.of(vehicle);
                    dto.setId(null); // Suppression de la clé-valeur id
                    return dto;
                })
                .toList();
    }

    public User getUser(Long userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if(userId == null){
            return userRepository.findByEmail(userDetails.getUsername());
        } else {
            return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        }
    }

}
