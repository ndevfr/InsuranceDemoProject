package fr.ndev.insurance.controller.InsurancePolicy;

import fr.ndev.insurance.dto.InsurancePolicyDTO;
import fr.ndev.insurance.service.InsurancePolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/agent/users")
@Tag(name = "agent_policies", description = "Endpoints for insurance policy management (user)")
public class InsurancePolicyManageController {

    private final InsurancePolicyService insuranceService;

    @Autowired
    InsurancePolicyManageController(InsurancePolicyService insuranceService) {
        this.insuranceService = insuranceService;
    }


    @Operation(summary = "Register a new policy", description = "Register a new policy")
    @PostMapping("{userId}/policies")
    public ResponseEntity<?> addInsurance(@RequestBody @Valid InsurancePolicyDTO insurancePolicyDTO, @PathVariable Long userId) {
        return insuranceService.addInsurance(insurancePolicyDTO, userId);
    }

    @Operation(summary = "Close a policy", description = "Close a policy")
    @DeleteMapping("{userId}/policies/{id}")
    public ResponseEntity<?> closeInsurance(@PathVariable int id, @PathVariable Long userId) {
        return insuranceService.closeInsurance(id, userId);
    }

    @Operation(summary = "Get a policy", description = "Get a policy")
    @GetMapping("{userId}/policies/{id}")
    public ResponseEntity<?> getInsurance(@PathVariable int id, @PathVariable Long userId) {
        return insuranceService.getInsurance(id, userId);
    }

    @Operation(summary = "Get user policies", description = "Get user policies")
    @GetMapping("{userId}/policies")
    public List<InsurancePolicyDTO> getAllInsurancePolicies(@PathVariable Long userId) {
        return insuranceService.getAllInsurancePolicies(userId);
    }
}