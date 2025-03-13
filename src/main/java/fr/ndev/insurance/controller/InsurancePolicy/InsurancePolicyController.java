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
@RequestMapping("/api/user")
@Tag(name = "policies", description = "Endpoints for insurance policy management (user)")
public class InsurancePolicyController {

    private final InsurancePolicyService insuranceService;

    @Autowired
    InsurancePolicyController(InsurancePolicyService insuranceService) {
        this.insuranceService = insuranceService;
    }


    @Operation(summary = "Register a new policy", description = "Register a new policy")
    @PostMapping("/policies")
    public ResponseEntity<?> addInsurance(@RequestBody @Valid InsurancePolicyDTO insurancePolicyDTO) {
        return insuranceService.addInsurance(insurancePolicyDTO, null);
    }

    @Operation(summary = "Close a policy", description = "Close a policy")
    @DeleteMapping("/policies/{id}")
    public ResponseEntity<?> closeInsurance(@PathVariable int id) {
        return insuranceService.closeInsurance(id, null);
    }

    @Operation(summary = "Get a policy", description = "Get a policy")
    @GetMapping("/policies/{id}")
    public ResponseEntity<?> getInsurance(@PathVariable int id) {
        return insuranceService.getInsurance(id, null);
    }

    @Operation(summary = "Get user policies", description = "Get user policies")
    @GetMapping("/policies")
    public List<InsurancePolicyDTO> getAllInsurancePolicies() {
        return insuranceService.getAllInsurancePolicies(null);
    }
}