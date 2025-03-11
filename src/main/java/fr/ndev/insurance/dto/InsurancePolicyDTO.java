package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ndev.insurance.model.InsurancePolicy;
import fr.ndev.insurance.model.User;
import fr.ndev.insurance.model.Vehicle;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.util.Date;

public class InsurancePolicyDTO {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String policyNumber;

    @NotBlank
    private String coverageType;

    @NotBlank
    private Date startDate;

    @NotBlank
    private Date endDate;

    @NotBlank
    private BigDecimal annualPremium;

    @NotBlank
    private Vehicle vehicle;

    @NotBlank
    private User user;

    public InsurancePolicyDTO() {}

    public InsurancePolicyDTO(Long id, String policyNumber, String coverageType, Date startDate, Date endDate, BigDecimal annualPremium, Vehicle vehicle, User user) {
        this.id = id;
        this.policyNumber = policyNumber;
        this.coverageType = coverageType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.annualPremium = annualPremium;
        this.vehicle = vehicle;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public String getCoverageType() {
        return coverageType;
    }

    public void setCoverageType(String coverageType) {
        this.coverageType = coverageType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getAnnualPremium() {
        return annualPremium;
    }

    public void setAnnualPremium(BigDecimal annualPremium) {
        this.annualPremium = annualPremium;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public InsurancePolicy toInsurancePolicy() {
        return new InsurancePolicy(policyNumber, coverageType, startDate, endDate, annualPremium, vehicle, user);
    }

    public static InsurancePolicyDTO of(InsurancePolicy insurancePolicy) {
        return new InsurancePolicyDTO(insurancePolicy.getId(), insurancePolicy.getPolicyNumber(), insurancePolicy.getCoverageType(), insurancePolicy.getStartDate(), insurancePolicy.getEndDate(), insurancePolicy.getAnnualPremium(), insurancePolicy.getVehicle(), insurancePolicy.getUser());
    }
}