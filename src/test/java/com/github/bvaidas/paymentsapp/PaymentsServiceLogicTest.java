package com.github.bvaidas.paymentsapp;

import com.github.bvaidas.paymentsapp.api.payload.PaymentRequest;
import com.github.bvaidas.paymentsapp.pojo.Payment;
import com.github.bvaidas.paymentsapp.pojo.PaymentChargeDetails;
import com.github.bvaidas.paymentsapp.service.PaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PaymentsServiceLogicTest {

    @Autowired
    private PaymentService paymentService;

    @Test
    public void validatePaymentRequestDetails() throws Exception {
        PaymentRequest req = createPaymentRequest();
        Payment payment = paymentService.createPayment(req);
        assertEquals(payment.getAmount(), req.getAmount());
        assertEquals(payment.getCurrency(), req.getCurrency());
        assertEquals(payment.getPaymentPurpose(), req.getPaymentPurpose());
        assertEquals(payment.getSender().getName(), req.getSenderName());
        assertEquals(payment.getSender().getAccountNumber(), req.getSenderAccountNumber());
        assertEquals(payment.getSender().getAccountNumberCode(), req.getSenderAccountNumberCode());
        assertEquals(payment.getReceiver().getName(), req.getReceiverName());
        assertEquals(payment.getReceiver().getAccountNumber(), req.getReceiverAccountNumber());
        assertEquals(payment.getReceiver().getAccountNumberCode(), req.getReceiverAccountNumberCode());
    }


    @Test
    public void validatePaymentCharges() throws Exception {
        //Dummy test
        PaymentRequest req = createPaymentRequest();
        PaymentChargeDetails chargeDetails = paymentService.calculatePaymentChargerDetails(req);
        assertEquals("SHAR", chargeDetails.getBearerCode());
        assertEquals(new BigDecimal("1.00"), chargeDetails.getReceiverChargesAmount());
        assertEquals("GBP", chargeDetails.getReceiverChargesCurrency());
    }


    public PaymentRequest createPaymentRequest() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(new BigDecimal(200));
        paymentRequest.setCurrency("GBP");
        paymentRequest.setPaymentPurpose("Server payment");
        paymentRequest.setSenderName("Wilfred Jeremiah Owens");
        paymentRequest.setSenderAccountNumber("31926819");
        paymentRequest.setSenderAccountNumberCode("BBAN");
        paymentRequest.setReceiverName("Emelia Jane Brown");
        paymentRequest.setReceiverAccountNumber("GB29XABC10161234567801");
        paymentRequest.setReceiverAccountNumberCode("IBAN");
        paymentRequest.setReference("Test reference");

        return paymentRequest;
    }


}
