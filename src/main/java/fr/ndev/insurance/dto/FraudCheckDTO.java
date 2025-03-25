package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.ndev.insurance.enums.FraudStatus;
import fr.ndev.insurance.model.Claim;
import fr.ndev.insurance.model.FraudCheck;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public class FraudCheckDTO {

    @JsonIgnore
    private Long id;

    @NotBlank
    @Schema(type="string", example="2025-01-01T00:00:00")
    private LocalDate checkDate;

    @Enumerated(EnumType.STRING)
    @Schema(type="string", example = "VERIFIED")
    private FraudStatus status;

    @NotBlank
    @Schema(type="string", example="Details...")
    private String details;

    @JsonIgnore
    private Claim claim;

    public FraudCheckDTO() {}

    public FraudCheckDTO(Long id, LocalDate checkDate, FraudStatus status, String details, Claim claim) {
        this.id = id;
        this.checkDate = checkDate;
        this.status = status;
        this.details = details;
        this.claim = claim;
    }

    public Long getId() {
        return id;
    }

    public FraudStatus getStatus() {
        return status;
    }

    public void setStatus(FraudStatus status) {
        this.status = status;
    }

    public FraudCheck toFraudCheck() {
        return new FraudCheck(checkDate, status, details, claim);
    }

    public static FraudCheckDTO of(FraudCheck fraudCheck) {
        return new FraudCheckDTO(fraudCheck.getId(), fraudCheck.getCheckDate(), fraudCheck.getStatus(), fraudCheck.getDetails(), fraudCheck.getClaim());
    }

}