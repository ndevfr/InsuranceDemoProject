package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.ndev.insurance.enums.FraudStatus;
import fr.ndev.insurance.enums.LitigationOutcome;
import fr.ndev.insurance.enums.LitigationStatus;
import fr.ndev.insurance.model.Claim;
import fr.ndev.insurance.model.Litigation;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class LitigationDTO {

    @JsonIgnore
    private Long id;

    @NotBlank
    @Schema(type="string", example="2025-01-01T00:00:00")
    private LocalDate startDate;

    @NotBlank
    @Schema(type="string", example="2025-01-01T00:00:00")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Schema(type="string", example = "RESOLVED")
    private LitigationStatus status;

    @Enumerated(EnumType.STRING)
    @Schema(type="string", example = "SETTLED")
    private LitigationOutcome outcome;

    @NotBlank
    @Schema(type="string", example="Details...")
    private String details;

    @JsonIgnore
    private Claim claim;

    public LitigationDTO() {}

    public LitigationDTO(Long id, LitigationStatus status, LitigationOutcome outcome, String details, Claim claim) {
        this.id = id;
        this.status = status;
        this.outcome = outcome;
        this.details = details;
        this.claim = claim;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LitigationStatus getStatus() {
        return status;
    }

    public void setStatus(LitigationStatus status) {
        this.status = status;
    }

    public LitigationOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(LitigationOutcome outcome) {
        this.outcome = outcome;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Claim getClaim() {
        return claim;
    }

    public void setClaim(Claim claim) {
        this.claim = claim;
    }

    public Litigation toLitigation() {
        return new Litigation(startDate, endDate, status, outcome, details, claim);
    }

}