package fr.ndev.insurance.controller.Claim;

import fr.ndev.insurance.dto.ClaimDTO;
import fr.ndev.insurance.dto.InsurancePolicyDTO;
import fr.ndev.insurance.service.ClaimService;
import fr.ndev.insurance.service.InsurancePolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@Tag(name = "claims", description = "Endpoints for claims management (user)")
public class ClaimController {

    private final ClaimService claimService;

    @Autowired
    ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }


    @Operation(summary = "Register a new claim", description = "Register a new claim")
    @PostMapping("/policies/{policyId}/claims")
    public ResponseEntity<?> addClaim(@PathVariable int policyId, @RequestBody @Valid ClaimDTO claimDTO) {
        return claimService.addClaim(claimDTO, policyId, null);
    }

    /* @Operation(summary = "Update a claim", description = "Update a claim")
    @PutMapping("/policies/{policyId}/claims/{id}")
    public ResponseEntity<?> updateClaim(@PathVariable int policyId, @PathVariable int id, @RequestBody @Valid ClaimDTO claimDTO) {
        return claimService.updateClaim(claimDTO, id, policyId, null);
    } */

    @Operation(summary = "Get a claim", description = "Get a claim")
    @GetMapping("/policies/{policyId}/claims/{id}")
    public ResponseEntity<?> getClaim(@PathVariable int policyId, @PathVariable int id) {
        return claimService.getClaim(id, policyId, null);
    }

    @Operation(summary = "Delete a claim", description = "Delete a claim")
    @DeleteMapping("/policies/{policyId}/claims/{id}")
    public ResponseEntity<?> deleteClaim(@PathVariable int policyId, @PathVariable int id) {
        return claimService.closeClaim(id, policyId, null);
    }

    @Operation(summary = "Get user claims", description = "Get user claims")
    @GetMapping("/policies/{policyId}/claims")
    public List<ClaimDTO> getAllClaims(@PathVariable int policyId) {
        return claimService.getAllClaims(policyId, null);
    }
}