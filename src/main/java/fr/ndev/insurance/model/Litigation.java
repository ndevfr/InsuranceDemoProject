package fr.ndev.insurance.model;

import fr.ndev.insurance.enums.LitigationOutcome;
import fr.ndev.insurance.enums.LitigationStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Litigation implements Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "status", nullable = false)
    private LitigationStatus status;

    @Column(name = "outcome", nullable = false)
    private LitigationOutcome outcome;

    @Column(name = "details", nullable = false)
    private String details;

    @ManyToOne
    @JoinColumn(name="claim_id", nullable=false)
    private Claim claim;

    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    public Litigation() {}

    public Litigation(LocalDate startDate, LocalDate endDate, LitigationStatus status, LitigationOutcome outcome, String details, Claim claim) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.outcome = outcome;
        this.details = details;
        this.claim = claim;
    }

    public Long getId() {
        return id;
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

    @Override
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
