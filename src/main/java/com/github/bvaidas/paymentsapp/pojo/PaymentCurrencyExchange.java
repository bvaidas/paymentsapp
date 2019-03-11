package com.github.bvaidas.paymentsapp.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Audited
@Entity
@Table(name = "CURRENCY_EXCHANGE", indexes = {@Index(columnList = "id", name = "payment_currency_exchanges_id_index")})

public class PaymentCurrencyExchange {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "uuid", strategy = "uuid4")
    @Column(updatable = false, nullable = false)
    private UUID id;


    @JsonProperty("contract_reference")
    private String contractReference;
    @JsonProperty("exchange_rate")
    @Column(name = "exchange_rate", scale = 5)
    private BigDecimal exchangeRate;
    @JsonProperty("original_amount")
    @Column(name = "originalAmount", scale = 2)
    private BigDecimal originalAmount;
    @JsonProperty("original_currency")
    private String originalCurrency;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContractReference() {
        return contractReference;
    }

    public void setContractReference(String contractReference) {
        this.contractReference = contractReference;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public BigDecimal getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(BigDecimal originalAmount) {
        this.originalAmount = originalAmount;
    }

    public String getOriginalCurrency() {
        return originalCurrency;
    }

    public void setOriginalCurrency(String originalCurrency) {
        this.originalCurrency = originalCurrency;
    }
}
