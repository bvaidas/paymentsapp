package com.github.bvaidas.paymentsapp.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Audited
@Entity
@Table(name = "PAYMENT_CHARGE_TRANSACTIONS", indexes = {@Index(columnList = "id", name = "payments_charge_transactions_id_index")})
public class PaymentChargeTransaction {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid", strategy = "uuid4")
    @Column(updatable = false, nullable = false)
    private UUID id;

    @Column(name = "receiver_charges_amount", scale = 2)
    @JsonProperty("receiver_charges_amount")
    private BigDecimal amount;

    @JsonProperty("currency")
    public String currency;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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
}
