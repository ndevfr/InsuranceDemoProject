package fr.ndev.insurance.service;

import fr.ndev.insurance.dto.*;
import fr.ndev.insurance.model.InsurancePolicy;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.model.Vehicle;
import fr.ndev.insurance.repository.InsurancePolicyRepository;
import fr.ndev.insurance.repository.UserRepository;
import fr.ndev.insurance.repository.VehicleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class InsurancePolicyService {

    private final UserService userService;
    private final InsurancePolicyRepository insuranceRepository;
    private final PasswordEncoder passwordEncoder;
    private final VehicleRepository vehicleRepository;

    @Autowired
    InsurancePolicyService(UserService userService, InsurancePolicyRepository insuranceRepository, PasswordEncoder passwordEncoder, InsurancePolicyRepository insurancePolicyRepository, VehicleRepository vehicleRepository) {
        this.userService = userService;
        this.insuranceRepository = insuranceRepository;
        this.passwordEncoder = passwordEncoder;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public ResponseEntity<?> addInsurance(InsurancePolicyDTO insurancePolicyDTO, Long userId) {
        try {
            User user = userService.getUser(userId);
            Vehicle vehicle = vehicleRepository.findByRegistrationNumber(insurancePolicyDTO.getVehicle()).orElseThrow(() -> new RuntimeException("Vehicle not found"));
            InsurancePolicy insurancePolicy = insurancePolicyDTO.toInsurancePolicy();
            insurancePolicy.setUser(user);
            insurancePolicy.setVehicle(vehicle);
            insurancePolicy.setBonusMalus(BigDecimal.ONE);
            insurancePolicy.setPolicyNumber(createPolicyNumber(insurancePolicy.getStartDate(), insuranceRepository.count()));
            insurancePolicy.setAnnualPremium(BigDecimal.valueOf(1000.0));
            insuranceRepository.save(insurancePolicy);
            return ResponseEntity.ok(InsurancePolicyDTO.of(insurancePolicy));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new JsonResponse(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> closeInsurance(int index, Long userId) {
        try {
            User user = userService.getUser(userId);
            InsurancePolicy closedInsurance =  getUserInsuranceById(user, index);
            closedInsurance.setEndDate(LocalDate.now());
            closedInsurance.setVehicle(null);
            insuranceRepository.save(closedInsurance);
            return ResponseEntity.ok(InsurancePolicyDTO.of(closedInsurance));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    public ResponseEntity<?> getInsurance(int index, Long userId) {
        try {
            User user = userService.getUser(userId);
            InsurancePolicy insurance =  getUserInsuranceById(user, index);
            return ResponseEntity.ok(InsurancePolicyDTO.of(insurance));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    public List<InsurancePolicyDTO> getAllInsurancePolicies(Long userId) {
        try {
            User user = userService.getUser(userId);

            return insuranceRepository.findByUser(user).stream()
                    .map(InsurancePolicyDTO::of)
                    .toList();
        } catch (RuntimeException e) {
            return null;
        }
    }

    public InsurancePolicy getInsuranceByVehicle(Vehicle vehicle) {
        try{
            return insuranceRepository.findByVehicle(vehicle).orElseThrow(() -> new RuntimeException("Insurance policy not found"));
        } catch (RuntimeException e) {
            return null;
        }
    }

    public InsurancePolicy getUserInsuranceById(User user, int index) {
        List<InsurancePolicy> insurances = insuranceRepository.findByUser(user);
        if (index <= 0 || index > insurances.size()) {
            throw new IndexOutOfBoundsException("Insurance Policy not found");
        }
        return insurances.get(index - 1);
    }

    public String createPolicyNumber(LocalDate startDate, long number){
        return "IP" + startDate.getYear() + startDate.getMonthValue() + String.format("%04d", number);
    }

}
