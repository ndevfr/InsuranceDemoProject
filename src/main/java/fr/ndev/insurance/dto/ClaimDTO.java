package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ndev.insurance.enums.ClaimStatus;
import fr.ndev.insurance.model.Claim;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ClaimDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private String claimNumber;

    @NotBlank
    private LocalDate accidentDate;

    @NotBlank
    private String description;

    private BigDecimal amountClaimed;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Double responsability;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Enumerated(EnumType.STRING)
    private ClaimStatus status;

    @JsonIgnore
    @NotBlank
    private InsurancePolicyDTO policy;

    public ClaimDTO() {}

    public ClaimDTO(Long id, String claimNumber, LocalDate accidentDate, String description, BigDecimal amountClaimed, Double responsability, ClaimStatus status, InsurancePolicyDTO policy) {
        this.id = id;
        this.claimNumber = claimNumber;
        this.accidentDate = accidentDate;
        this.description = description;
        this.amountClaimed = amountClaimed;
        this.responsability = responsability;
        this.status = status;
        this.policy = policy;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getAccidentDate() {
        return accidentDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmountClaimed() {
        return amountClaimed;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public Claim toClaim() {
        return new Claim(claimNumber, accidentDate, description, amountClaimed, responsability, status, policy.toInsurancePolicy());
    }

    public static ClaimDTO of(Claim claim) {
        return new ClaimDTO(claim.getId(), claim.getClaimNumber(), claim.getAccidentDate(), claim.getDescription(), claim.getAmountClaimed(), claim.getResponsability(), claim.getStatus(), InsurancePolicyDTO.of(claim.getPolicy()));
    }
}