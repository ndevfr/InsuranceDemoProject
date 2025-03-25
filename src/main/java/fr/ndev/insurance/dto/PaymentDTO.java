package fr.ndev.insurance.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.ndev.insurance.enums.PaymentMethod;
import fr.ndev.insurance.enums.PaymentStatus;
import fr.ndev.insurance.model.InsurancePolicy;
import fr.ndev.insurance.model.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDTO {

    @JsonIgnore
    private Long id;

    @NotBlank
    @Schema(type="string", example="2025-01-01T00:00:00")
    private LocalDateTime paymentDate;

    @NotBlank
    @Schema(type="string", example = "1234.63")
    private BigDecimal amount;

    @NotBlank
    @Schema(type="string", example="CREDIT_CARD")
    private PaymentMethod paymentMethod;

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(type="string", example="PENDING")
    private PaymentStatus status;

    @JsonIgnore
    private InsurancePolicy insurancePolicy;

    public PaymentDTO() {}

    public PaymentDTO(Long id, LocalDateTime paymentDate, BigDecimal amount, PaymentMethod paymentMethod, PaymentStatus status, InsurancePolicy insurancePolicy) {
        this.id = id;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.insurancePolicy = insurancePolicy;
    }

    public Long getId() {
        return id;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public Payment toPayment() {
        return new Payment(paymentDate, amount, paymentMethod, status, insurancePolicy);
    }

    public static PaymentDTO of(Payment payment) {
        return new PaymentDTO(payment.getId(), payment.getPaiementDate(), payment.getAmount(), payment.getPaymentMethod(), payment.getStatus(), payment.getInsurancePolicy());
    }

}