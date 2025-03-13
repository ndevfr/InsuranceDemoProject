package fr.ndev.insurance.model;

import fr.ndev.insurance.enums.PaymentMethod;
import fr.ndev.insurance.enums.PaymentStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "paiement_date", nullable = false)
    private LocalDateTime paiementDate;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "payment_method", nullable = false)
    private PaymentMethod paymentMethod;

    @Column(name = "status", nullable = false)
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name="policy_id", nullable=false)
    private InsurancePolicy insurancePolicy;

    public Payment() {}

    public Payment(LocalDateTime paiementDate, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status, InsurancePolicy insurancePolicy) {
        this.paiementDate = paiementDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.insurancePolicy = insurancePolicy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getPaiementDate() {
        return paiementDate;
    }

    public void setPaiementDate(LocalDateTime paiementDate) {
        this.paiementDate = paiementDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public InsurancePolicy getInsurancePolicy() {
        return insurancePolicy;
    }

    public void setInsurancePolicy(InsurancePolicy insurancePolicy) {
        this.insurancePolicy = insurancePolicy;
    }
}
