package com.github.bvaidas.paymentsapp.service.exceptions;

public class PaymentAccountNotFoundException extends Exception {
    public PaymentAccountNotFoundException(String message) {
        super(message);
    }
}