package com.github.bvaidas.paymentsapp.service;

import com.github.bvaidas.paymentsapp.api.payload.PaymentRequest;
import com.github.bvaidas.paymentsapp.pojo.*;
import com.github.bvaidas.paymentsapp.repository.AccountRepository;
import com.github.bvaidas.paymentsapp.repository.PaymentsRepository;
import com.github.bvaidas.paymentsapp.service.exceptions.PaymentAccountNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

@Service
public class PaymentService {

    private Logger log = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Payment createPayment(PaymentRequest paymentRequest) throws PaymentAccountNotFoundException {
        //Find sender account by paymentRequest payload
        AccountInfo sender = accountRepository.findOneByAccountNumberAndAccountNumberCodeAndName(paymentRequest.getSenderAccountNumber(), paymentRequest.getSenderAccountNumberCode(), paymentRequest.getSenderName());

        if (sender == null) {
            throw new PaymentAccountNotFoundException("Sender account not found");
        }
        //Find receiver account by paymentRequest payload
        AccountInfo receiver = accountRepository.findOneByAccountNumberAndAccountNumberCodeAndName(paymentRequest.getReceiverAccountNumber(), paymentRequest.getReceiverAccountNumberCode(), paymentRequest.getReceiverName());

        if (receiver == null) {
            throw new PaymentAccountNotFoundException("Receiver account not found");
        }

        //Populate payment object from Payment Request - adds payment details
        Payment payment = populatePaymentFromPaymentRequest(paymentRequest);
        payment.setSender(sender);
        payment.setReceiver(receiver);
        //Calculate payment charge details from payment request and payment initial details. Could be used to calculate discounts and etc.
        PaymentChargeDetails chargeDetails = calculatePaymentChargerDetails(paymentRequest);
        payment.setPaymentChargeDetails(chargeDetails);
        //Apply currency exchange rates
        chargeDetails.setPaymentCurrencyExchange(currencyExchange(payment, paymentRequest));

        paymentsRepository.save(payment);
        log.info("Created new payment :", payment);
        return payment;
    }

    public PaymentChargeDetails calculatePaymentChargerDetails(PaymentRequest paymentRequest) {

        PaymentChargeDetails chargeDetails = new PaymentChargeDetails();
        chargeDetails.setBearerCode("SHAR");
        chargeDetails.setReceiverChargesAmount(new BigDecimal("1.00"));
        chargeDetails.setReceiverChargesCurrency("GBP");

        //Apply static transaction charges
        PaymentChargeTransaction standardCost = new PaymentChargeTransaction();
        standardCost.setAmount(new BigDecimal("5.0"));
        standardCost.setCurrency("GBP");
        PaymentChargeTransaction additionalCost = new PaymentChargeTransaction();
        additionalCost.setAmount(new BigDecimal("10.0"));
        additionalCost.setCurrency("GBP");


        chargeDetails.getSenderCharges().add(standardCost);
        chargeDetails.getSenderCharges().add(additionalCost);


        return chargeDetails;
    }

    public PaymentCurrencyExchange currencyExchange(Payment payment, PaymentRequest paymentRequest) {

        PaymentCurrencyExchange exchange = new PaymentCurrencyExchange();
        exchange.setContractReference("FX123");
        exchange.setExchangeRate(new BigDecimal("2.0000"));
        exchange.setOriginalAmount(new BigDecimal("200.42"));
        exchange.setOriginalCurrency("USD");
        return exchange;

    }

    public Payment populatePaymentFromPaymentRequest(PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        payment.setAmount(paymentRequest.getAmount());
        payment.setCurrency(paymentRequest.getCurrency());
        payment.setNumericReference(payment.getNumericReference());
        payment.setPaymentPurpose(paymentRequest.getPaymentPurpose());
        payment.setReference(paymentRequest.getReference());
        payment.setEndToEndReference("Wil piano Jan");
        payment.setNumericReference("1002001");
        payment.setPaymentId("123456789012345678");
        payment.setPaymentScheme("FPS");
        payment.setPaymentType("Credit");
        payment.setProcessingDate(new Date());
        payment.setSchemePaymentSubType("InternetBanking");
        payment.setSchemePaymentType("ImmediatePayment");
        payment.setSponsorAccountNumber("56781234");
        payment.setSponsorBankId("123123");
        payment.setSponsorBankIdCode("GBDSC");
        return payment;
    }
}
