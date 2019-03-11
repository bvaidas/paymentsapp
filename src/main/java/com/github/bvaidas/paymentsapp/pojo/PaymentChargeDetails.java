package com.github.bvaidas.paymentsapp.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Audited
@Entity
@Table(name = "PAYMENT_CHARGE_INFORMATION", indexes = {@Index(columnList = "id", name = "payment_charge_id_index")})
public class PaymentChargeDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid", strategy = "uuid4")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @JsonProperty("bearer_code")
    private String bearerCode;
    @JsonProperty("sender_charges")

    @ManyToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    PaymentCurrencyExchange paymentCurrencyExchange;

    @Valid
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "PAYMENTS_CHARGE_INFORMATION_ID")
    private List<PaymentChargeTransaction> senderCharges = new ArrayList<PaymentChargeTransaction>();

    @Column(name = "receiver_charges_amount", scale = 2)
    @JsonProperty("receiver_charges_amount")
    BigDecimal receiverChargesAmount;
    @JsonProperty("receiver_charges_currency")
    private String receiverChargesCurrency;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getBearerCode() {
        return bearerCode;
    }

    public void setBearerCode(String bearerCode) {
        this.bearerCode = bearerCode;
    }


    public List<PaymentChargeTransaction> getSenderCharges() {
        return senderCharges;
    }

    public void setSenderCharges(List<PaymentChargeTransaction> senderCharges) {
        this.senderCharges = senderCharges;
    }

    public BigDecimal getReceiverChargesAmount() {
        return receiverChargesAmount;
    }

    public void setReceiverChargesAmount(BigDecimal receiverChargesAmount) {
        this.receiverChargesAmount = receiverChargesAmount;
    }

    public String getReceiverChargesCurrency() {
        return receiverChargesCurrency;
    }

    public void setReceiverChargesCurrency(String receiverChargesCurrency) {
        this.receiverChargesCurrency = receiverChargesCurrency;
    }

    public PaymentCurrencyExchange getPaymentCurrencyExchange() {
        return paymentCurrencyExchange;
    }

    public void setPaymentCurrencyExchange(PaymentCurrencyExchange paymentCurrencyExchange) {
        this.paymentCurrencyExchange = paymentCurrencyExchange;
    }
}
