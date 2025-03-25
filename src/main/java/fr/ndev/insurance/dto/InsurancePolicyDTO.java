package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ndev.insurance.enums.CoverageType;
import fr.ndev.insurance.model.InsurancePolicy;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.model.Vehicle;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public class InsurancePolicyDTO {

    @JsonIgnore
    private Long id;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(type="string", example="2025030001")
    private String policyNumber;

    @Enumerated(EnumType.STRING)
    @Schema(type="string", example="LIABILITY")
    private CoverageType coverageType;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(type="double", example="1000.00")
    private BigDecimal annualPremium;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(type="double", example="1.0")
    private BigDecimal bonusMalus;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(type="string", example="AA-123-AA")
    private String vehicleLast;

    @NotBlank
    @Schema(type="string", example="AA-123-AA")
    private String vehicle;

    @JsonIgnore
    private User user;

    public InsurancePolicyDTO() {}

    public InsurancePolicyDTO(Long id, String policyNumber, CoverageType coverageType, LocalDate startDate, LocalDate endDate, BigDecimal annualPremium, BigDecimal bonusMalus, String vehicleLast, String vehicle, User user) {
        this.id = id;
        this.policyNumber = policyNumber;
        this.coverageType = coverageType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.annualPremium = annualPremium;
        this.bonusMalus = bonusMalus;
        this.vehicleLast = vehicleLast;
        this.vehicle = vehicle;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InsurancePolicy toInsurancePolicy() {
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber(this.vehicle);
        return new InsurancePolicy(policyNumber, coverageType, startDate, endDate, annualPremium, bonusMalus, vehicle, user);
    }

    public static InsurancePolicyDTO of(InsurancePolicy insurancePolicy) {
        String registrationNumber = null;
        if (insurancePolicy.getVehicle() != null) {
            registrationNumber = insurancePolicy.getVehicle().getRegistrationNumber();
        }

        return new InsurancePolicyDTO(insurancePolicy.getId(), insurancePolicy.getPolicyNumber(), insurancePolicy.getCoverageType(), insurancePolicy.getStartDate(), insurancePolicy.getEndDate(), insurancePolicy.getAnnualPremium(), insurancePolicy.getBonusMalus(), insurancePolicy.getVehicleLast(), registrationNumber, insurancePolicy.getUser());
    }
}