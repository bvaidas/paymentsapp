package com.github.bvaidas.paymentsapp.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Audited
@Entity
@Table(name = "PAYMENTS", indexes = {@Index(columnList = "id", name = "payment_id_index")})
public class Payment {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid", strategy = "uuid4")
    @Column(updatable = false, nullable = false)
    @NotNull
    private UUID id;

    @Version
    private Integer version;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @NotNull
    private AccountInfo sender;

    @ManyToOne
    @PrimaryKeyJoinColumn
    @NotNull
    private AccountInfo receiver;

    @ManyToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @NotNull
    private PaymentChargeDetails paymentChargeDetails;

    @Column(name = "amount", scale = 3)
    @JsonProperty("amount")
    @NotNull
    BigDecimal amount;

    @JsonProperty("currency")
    @NotEmpty
    String currency;

    @JsonProperty("numeric_reference")
    private String numericReference;

    @JsonProperty("end_to_end_reference")
    private String endToEndReference;

    @JsonProperty("payment_purpose")
    private String paymentPurpose;

    @JsonProperty("payment_scheme")
    private String paymentScheme;

    @JsonProperty("payment_type")
    private String paymentType;

    @JsonProperty("processing_date")
    @Temporal(TemporalType.DATE)
    private Date processingDate;

    @JsonProperty("payment_id")
    private String paymentId;


    @JsonProperty("reference")
    private String reference;

    @JsonProperty("scheme_payment_sub_type")
    private String schemePaymentSubType;

    @Column(length = 255)
    @JsonProperty("scheme_payment_type")
    private String schemePaymentType;


    @JsonProperty("sponsor_account_number")
    public String sponsorAccountNumber;
    @JsonProperty("sponsor_bank_id")
    public String sponsorBankId;
    @JsonProperty("sponsor_bank_id_code")
    public String sponsorBankIdCode;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public AccountInfo getSender() {
        return sender;
    }

    public void setSender(AccountInfo sender) {
        this.sender = sender;
    }

    public AccountInfo getReceiver() {
        return receiver;
    }

    public void setReceiver(AccountInfo receiver) {
        this.receiver = receiver;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getNumericReference() {
        return numericReference;
    }

    public void setNumericReference(String numericReference) {
        this.numericReference = numericReference;
    }

    public String getEndToEndReference() {
        return endToEndReference;
    }

    public void setEndToEndReference(String endToEndReference) {
        this.endToEndReference = endToEndReference;
    }

    public String getPaymentPurpose() {
        return paymentPurpose;
    }

    public void setPaymentPurpose(String paymentPurpose) {
        this.paymentPurpose = paymentPurpose;
    }

    public String getPaymentScheme() {
        return paymentScheme;
    }

    public void setPaymentScheme(String paymentScheme) {
        this.paymentScheme = paymentScheme;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Date getProcessingDate() {
        return processingDate;
    }

    public void setProcessingDate(Date processingDate) {
        this.processingDate = processingDate;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSchemePaymentSubType() {
        return schemePaymentSubType;
    }

    public void setSchemePaymentSubType(String schemePaymentSubType) {
        this.schemePaymentSubType = schemePaymentSubType;
    }

    public String getSchemePaymentType() {
        return schemePaymentType;
    }

    public void setSchemePaymentType(String schemePaymentType) {
        this.schemePaymentType = schemePaymentType;
    }

    public String getSponsorAccountNumber() {
        return sponsorAccountNumber;
    }

    public void setSponsorAccountNumber(String sponsorAccountNumber) {
        this.sponsorAccountNumber = sponsorAccountNumber;
    }

    public String getSponsorBankId() {
        return sponsorBankId;
    }

    public void setSponsorBankId(String sponsorBankId) {
        this.sponsorBankId = sponsorBankId;
    }

    public String getSponsorBankIdCode() {
        return sponsorBankIdCode;
    }

    public void setSponsorBankIdCode(String sponsorBankIdCode) {
        this.sponsorBankIdCode = sponsorBankIdCode;
    }

    public PaymentChargeDetails getPaymentChargeDetails() {
        return paymentChargeDetails;
    }

    public void setPaymentChargeDetails(PaymentChargeDetails paymentChargeDetails) {
        this.paymentChargeDetails = paymentChargeDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id) &&
                Objects.equals(version, payment.version) &&
                Objects.equals(sender, payment.sender) &&
                Objects.equals(receiver, payment.receiver) &&
                Objects.equals(paymentChargeDetails, payment.paymentChargeDetails) &&
                Objects.equals(amount, payment.amount) &&
                Objects.equals(currency, payment.currency) &&
                Objects.equals(numericReference, payment.numericReference) &&
                Objects.equals(endToEndReference, payment.endToEndReference) &&
                Objects.equals(paymentPurpose, payment.paymentPurpose) &&
                Objects.equals(paymentScheme, payment.paymentScheme) &&
                Objects.equals(paymentType, payment.paymentType) &&
                Objects.equals(processingDate, payment.processingDate) &&
                Objects.equals(paymentId, payment.paymentId) &&
                Objects.equals(reference, payment.reference) &&
                Objects.equals(schemePaymentSubType, payment.schemePaymentSubType) &&
                Objects.equals(schemePaymentType, payment.schemePaymentType) &&
                Objects.equals(sponsorAccountNumber, payment.sponsorAccountNumber) &&
                Objects.equals(sponsorBankId, payment.sponsorBankId) &&
                Objects.equals(sponsorBankIdCode, payment.sponsorBankIdCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, sender, receiver, paymentChargeDetails, amount, currency, numericReference, endToEndReference, paymentPurpose, paymentScheme, paymentType, processingDate, paymentId, reference, schemePaymentSubType, schemePaymentType, sponsorAccountNumber, sponsorBankId, sponsorBankIdCode);
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", version=" + version +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", paymentChargeDetails=" + paymentChargeDetails +
                ", amount=" + amount +
                ", currency='" + currency + '\'' +
                ", numericReference='" + numericReference + '\'' +
                ", endToEndReference='" + endToEndReference + '\'' +
                ", paymentPurpose='" + paymentPurpose + '\'' +
                ", paymentScheme='" + paymentScheme + '\'' +
                ", paymentType='" + paymentType + '\'' +
                ", processingDate=" + processingDate +
                ", paymentId='" + paymentId + '\'' +
                ", reference='" + reference + '\'' +
                ", schemePaymentSubType='" + schemePaymentSubType + '\'' +
                ", schemePaymentType='" + schemePaymentType + '\'' +
                ", sponsorAccountNumber='" + sponsorAccountNumber + '\'' +
                ", sponsorBankId='" + sponsorBankId + '\'' +
                ", sponsorBankIdCode='" + sponsorBankIdCode + '\'' +
                '}';
    }
}
