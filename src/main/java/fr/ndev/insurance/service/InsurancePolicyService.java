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

    private final UserRepository userRepository;
    private final InsurancePolicyRepository insuranceRepository;
    private final PasswordEncoder passwordEncoder;
    private final VehicleRepository vehicleRepository;

    @Autowired
    InsurancePolicyService(UserRepository userRepository, InsurancePolicyRepository insuranceRepository, PasswordEncoder passwordEncoder, InsurancePolicyRepository insurancePolicyRepository, VehicleRepository vehicleRepository) {
        this.userRepository = userRepository;
        this.insuranceRepository = insuranceRepository;
        this.passwordEncoder = passwordEncoder;
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public ResponseEntity<?> addInsurance(InsurancePolicyDTO insurancePolicyDTO, Long userId) {
        User user = getUser(userId);
        Vehicle vehicle = vehicleRepository.findByRegistrationNumber(insurancePolicyDTO.getVehicle());
        if (vehicle == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, "Vehicle not found"));
        }
        InsurancePolicy insurancePolicy = insurancePolicyDTO.toInsurancePolicy();
        insurancePolicy.setUser(user);
        insurancePolicy.setVehicle(vehicle);
        insurancePolicy.setBonusMalus(BigDecimal.valueOf(1.0));
        insurancePolicy.setPolicyNumber(createPolicyNumber(insurancePolicy.getStartDate(), insuranceRepository.count()));
        insurancePolicy.setAnnualPremium(BigDecimal.valueOf(1000.0));
        insuranceRepository.save(insurancePolicy);
        return ResponseEntity.ok(InsurancePolicyDTO.of(insurancePolicy));
    }

    @Transactional
    public ResponseEntity<?> closeInsurance(int index, Long userId) {
        User user = getUser(userId);
        index = index - 1;
        List<InsurancePolicy> insurances = insuranceRepository.findByUser(user);
        int insurancesCount = insurances.size();
        if(index < 0 || index >= insurancesCount) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, "Insurance policy not found"));
        }
        InsurancePolicy closedInsurance = insurances.get(index);
        closedInsurance.setEndDate(LocalDate.now());
        insuranceRepository.save(closedInsurance);
        return ResponseEntity.ok(InsurancePolicyDTO.of(closedInsurance));
    }

    public ResponseEntity<?> getInsurance(int index, Long userId) {
        User user = getUser(userId);
        index = index - 1;
        List<InsurancePolicy> insurances = insuranceRepository.findByUser(user);
        int insurancesCount = insurances.size();
        if(index < 0 || index >= insurancesCount) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, "Insurance policy not found"));
        }
        InsurancePolicy closedInsurance = insurances.get(index);
        return ResponseEntity.ok(InsurancePolicyDTO.of(closedInsurance));
    }

    public List<InsurancePolicyDTO> getAllInsurancePolicies(Long userId) {
        User user = getUser(userId);

        return insuranceRepository.findByUser(user).stream()
                .map(InsurancePolicyDTO::of)
                .toList();
    }

    public String createPolicyNumber(LocalDate startDate, long number){
        return "IP" + startDate.getYear() + startDate.getMonthValue() + String.format("%04d", number);
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
