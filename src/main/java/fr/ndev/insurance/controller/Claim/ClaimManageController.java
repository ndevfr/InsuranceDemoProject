package fr.ndev.insurance.controller.Claim;

import fr.ndev.insurance.dto.ClaimDTO;
import fr.ndev.insurance.service.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent/users")
@Tag(name = "agent_claims", description = "Endpoints for claims management (agent/admin)")
public class ClaimManageController {

    private final ClaimService claimService;

    @Autowired
    ClaimManageController(ClaimService claimService) {
        this.claimService = claimService;
    }


    @Operation(summary = "Register a new claim", description = "Register a new claim")
    @PostMapping("/{userId}/policies/{policyId}/claims")
    public ResponseEntity<?> addClaim(@PathVariable int policyId, @RequestBody @Valid ClaimDTO claimDTO, @PathVariable Long userId) {
        return claimService.addClaim(claimDTO, policyId, userId);
    }

    @Operation(summary = "Update a claim", description = "Update a claim")
    @PutMapping("/{userId}/policies/{policyId}/claims/{id}")
    public ResponseEntity<?> updateClaim(@PathVariable int policyId, @PathVariable int id, @RequestBody @Valid ClaimDTO claimDTO, @PathVariable Long userId) {
        return claimService.updateClaim(claimDTO, id, policyId, userId);
    }

    @Operation(summary = "Get a claim", description = "Get a claim")
    @GetMapping("/{userId}/policies/{policyId}/claims/{id}")
    public ResponseEntity<?> getClaim(@PathVariable int policyId, @PathVariable int id, @PathVariable Long userId) {
        return claimService.getClaim(id, policyId, userId);
    }

    @Operation(summary = "Delete a claim", description = "Delete a claim")
    @DeleteMapping("/{userId}/policies/{policyId}/claims/{id}")
    public ResponseEntity<?> deleteClaim(@PathVariable int policyId, @PathVariable int id, @PathVariable Long userId) {
        return claimService.closeClaim(id, policyId, userId);
    }

    @Operation(summary = "Get user claims", description = "Get user claims")
    @GetMapping("/{userId}/policies/{policyId}/claims")
    public List<ClaimDTO> getAllClaims(@PathVariable int policyId, @PathVariable Long userId) {
        return claimService.getAllClaims(policyId, userId);
    }

}