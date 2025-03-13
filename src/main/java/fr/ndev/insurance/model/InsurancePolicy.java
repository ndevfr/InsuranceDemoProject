package fr.ndev.insurance.model;

import fr.ndev.insurance.enums.CoverageType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "insurance_policies")
public class InsurancePolicy implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "policy_number", nullable = false)
    private String policyNumber;

    @Column(name = "coverage_type", nullable = false)
    private CoverageType coverageType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "annual_premium", nullable = false)
    private BigDecimal annualPremium;

    @Column(name = "bonus_malus", nullable = false)
    private BigDecimal bonusMalus;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public InsurancePolicy() {}

    public InsurancePolicy(String policyNumber, CoverageType coverageType, LocalDate startDate, LocalDate endDate, BigDecimal annualPremium, BigDecimal bonusMalus, Vehicle vehicle, User user) {
        this.policyNumber = policyNumber;
        this.coverageType = coverageType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.annualPremium = annualPremium;
        this.bonusMalus = new BigDecimal(1);
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

    public CoverageType getCoverageType() {
        return coverageType;
    }

    public void setCoverageType(CoverageType coverageType) {
        this.coverageType = coverageType;
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

    public BigDecimal getAnnualPremium() {
        return annualPremium;
    }

    public void setAnnualPremium(BigDecimal annualPremium) {
        this.annualPremium = annualPremium;
    }

    public BigDecimal getBonusMalus() {
        return bonusMalus;
    }

    public void setBonusMalus(BigDecimal bonusMalus) {
        this.bonusMalus = bonusMalus;
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

    @Override
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}