package fr.ndev.insurance.service;

import fr.ndev.insurance.dto.ClaimDTO;
import fr.ndev.insurance.dto.InsurancePolicyDTO;
import fr.ndev.insurance.dto.JsonResponse;
import fr.ndev.insurance.enums.ClaimStatus;
import fr.ndev.insurance.model.Claim;
import fr.ndev.insurance.model.InsurancePolicy;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.model.Vehicle;
import fr.ndev.insurance.repository.ClaimRepository;
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
public class ClaimService {

    private final UserService userService;
    private final InsurancePolicyRepository insuranceRepository;
    private final PasswordEncoder passwordEncoder;
    private final VehicleRepository vehicleRepository;
    private final ClaimRepository claimRepository;
    private final InsurancePolicyService insurancePolicyService;

    @Autowired
    ClaimService(UserService userService, InsurancePolicyRepository insuranceRepository, ClaimRepository claimRepository, PasswordEncoder passwordEncoder, InsurancePolicyRepository insurancePolicyRepository, VehicleRepository vehicleRepository, InsurancePolicyService insurancePolicyService) {
        this.userService = userService;
        this.insuranceRepository = insuranceRepository;
        this.passwordEncoder = passwordEncoder;
        this.claimRepository = claimRepository;
        this.vehicleRepository = vehicleRepository;
        this.insurancePolicyService = insurancePolicyService;
    }

    @Transactional
    public ResponseEntity<?> addClaim(ClaimDTO claimDTO, int policyId, Long userId) {
        try {
            User user = userService.getUser(userId);
            InsurancePolicy insurancePolicy = insurancePolicyService.getUserInsuranceById(user, policyId);
            int claimsCount = claimRepository.countByPolicy(insurancePolicy);
            if (insurancePolicy == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, "Insurance policy not found"));
            }
            Claim claim = claimDTO.toClaim();
            claim.setPolicy(insurancePolicy);
            claim.setClaimNumber(createClaimNumber(insurancePolicy.getPolicyNumber(), claimsCount + 1));
            if (claim.getAmountClaimed() == null) {
                claim.setAmountClaimed(BigDecimal.ZERO);
            }
            claim.setStatus(ClaimStatus.PENDING);
            claimRepository.save(claim);
            return ResponseEntity.ok(ClaimDTO.of(claim));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> updateClaim(ClaimDTO claimDTO, int index, int policyId, Long userId){
       try {
           User user = userService.getUser(userId);
            Claim claim = getUserClaimById(user, policyId, index);
            claim.setDescription(claimDTO.getDescription());
            claim.setAmountClaimed(claimDTO.getAmountClaimed());
            claim.setAccidentDate(claimDTO.getAccidentDate());
            claimRepository.save(claim);
            return ResponseEntity.ok(ClaimDTO.of(claim));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> closeClaim(int index, int policyId, Long userId) {
        try {
            User user = userService.getUser(userId);
            Claim closedClaim = getUserClaimById(user, policyId, index);
            closedClaim.setStatus(ClaimStatus.CANCELLED);
            claimRepository.save(closedClaim);
            return ResponseEntity.ok(ClaimDTO.of(closedClaim));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    public ResponseEntity<?> getClaim(int index, int policyId, Long userId) {
        try {
            User user = userService.getUser(userId);
            Claim claim = getUserClaimById(user, policyId, index);
            return ResponseEntity.ok(ClaimDTO.of(claim));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new JsonResponse(HttpStatus.NOT_FOUND, e.getMessage()));
        }
    }

    public List<ClaimDTO> getAllClaims(int policyId, Long userId) {
        try {
            User user = userService.getUser(userId);
            InsurancePolicy insurancePolicy = insurancePolicyService.getUserInsuranceById(user, policyId);
            return claimRepository.findByPolicy(insurancePolicy).stream()
                    .map(ClaimDTO::of)
                    .toList();
        } catch (RuntimeException e) {
            return null;
        }
    }

    public Claim getUserClaimById(User user, int policyId, int index) {
        InsurancePolicy insurancePolicy = insurancePolicyService.getUserInsuranceById(user, policyId);
        List<Claim> claims = claimRepository.findByPolicy(insurancePolicy);
        if (index <= 0 || index > claims.size()) {
            throw new IndexOutOfBoundsException("Insurance Policy not found");
        }
        return claims.get(index - 1);
    }

    public String createClaimNumber(String policyName, long number){
        return policyName + "-" + String.format("%03d", number);
    }

}
