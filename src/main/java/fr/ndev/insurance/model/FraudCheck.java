package fr.ndev.insurance.model;

import fr.ndev.insurance.enums.FraudStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class FraudCheck  implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "check_date", nullable = false)
    private LocalDate checkDate;

    @Column(name = "status", nullable = false)
    private FraudStatus status;

    @Column(name = "details", nullable = false)
    private String details;

    @ManyToOne
    @JoinColumn(name="claim_id", nullable=false)
    private Claim claim;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    public FraudCheck() {}

    public FraudCheck(LocalDate checkDate, FraudStatus status, String details, Claim claim) {
        this.checkDate = checkDate;
        this.status = status;
        this.details = details;
        this.claim = claim;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(LocalDate checkDate) {
        this.checkDate = checkDate;
    }

    public FraudStatus getStatus() {
        return status;
    }

    public void setStatus(FraudStatus status) {
        this.status = status;
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

    @Override
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
