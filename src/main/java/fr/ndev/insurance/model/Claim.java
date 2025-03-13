package fr.ndev.insurance.model;

import fr.ndev.insurance.enums.ClaimStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "claims")
public class Claim implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "claim_number")
    private String claimNumber;

    @Column(name = "accident_date")
    private LocalDate accidentDate;

    @Column(name = "description")
    private String description;

    @Column(name = "amount_claimed")
    private BigDecimal amountClaimed;

    @Column(name="responsability")
    private Double responsability;

    @Column(name = "status")
    private ClaimStatus status;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "policy_id")
    private InsurancePolicy policy;

    public Claim() {}

    public Claim(String claimNumber, LocalDate accidentDate, String description, BigDecimal amountClaimed, Double responsability, ClaimStatus status, InsurancePolicy policy) {
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

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(String claimNumber) {
        this.claimNumber = claimNumber;
    }

    public LocalDate getAccidentDate() {
        return accidentDate;
    }

    public void setAccidentDate(LocalDate accidentDate) {
        this.accidentDate = accidentDate;
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

    public void setAmountClaimed(BigDecimal amountClaimed) {
        this.amountClaimed = amountClaimed;
    }

    public Double getResponsability() {
        return responsability;
    }

    public void setResponsability(Double responsability) {
        this.responsability = responsability;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public InsurancePolicy getPolicy() {
        return policy;
    }

    public void setPolicy(InsurancePolicy policy) {
        this.policy = policy;
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