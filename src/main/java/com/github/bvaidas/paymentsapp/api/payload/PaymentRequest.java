package com.github.bvaidas.paymentsapp.api.payload;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

public class PaymentRequest {


    @JsonProperty("sender_name")
    @NotBlank(message = "Sender Name is mandatory")
    private String senderName;
    @JsonProperty("sender_account_number")
    @NotBlank(message = "Sender Account Number is mandatory")
    private String senderAccountNumber;
    @JsonProperty("sender_account_number_code")
    @NotBlank(message = "Sender Account Number Code is mandatory")
    private String senderAccountNumberCode;
    @JsonProperty("receiver_name")
    @NotBlank(message = "Receiver Name is mandatory")
    private String receiverName;
    @JsonProperty("receiver_account_number")
    @NotBlank(message = "Receiver Account Number is mandatory")
    private String receiverAccountNumber;
    @JsonProperty("receiver_account_number_code")
    @NotBlank(message = "Receiver Account Number Code is mandatory")
    private String receiverAccountNumberCode;
    @JsonProperty("payment_purpose")
    @NotBlank(message = "Payment Purpose is mandatory")
    private String paymentPurpose;

    @JsonProperty("currency")
    @NotBlank(message = "Currency code is mandatory")
    private String currency;

    @JsonProperty("amount")
    @DecimalMin("0.01")
    private BigDecimal amount;

    @JsonProperty("reference")
    private String reference;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSenderAccountNumber() {
        return senderAccountNumber;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    public String getSenderAccountNumberCode() {
        return senderAccountNumberCode;
    }

    public void setSenderAccountNumberCode(String senderAccountNumberCode) {
        this.senderAccountNumberCode = senderAccountNumberCode;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverAccountNumber() {
        return receiverAccountNumber;
    }

    public void setReceiverAccountNumber(String receiverAccountNumber) {
        this.receiverAccountNumber = receiverAccountNumber;
    }

    public String getReceiverAccountNumberCode() {
        return receiverAccountNumberCode;
    }

    public void setReceiverAccountNumberCode(String receiverAccountNumberCode) {
        this.receiverAccountNumberCode = receiverAccountNumberCode;
    }

    public String getPaymentPurpose() {
        return paymentPurpose;
    }

    public void setPaymentPurpose(String paymentPurpose) {
        this.paymentPurpose = paymentPurpose;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }
}
