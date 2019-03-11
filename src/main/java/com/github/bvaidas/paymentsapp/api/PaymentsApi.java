package com.github.bvaidas.paymentsapp.api;

import com.github.bvaidas.paymentsapp.api.payload.PaymentRequest;
import com.github.bvaidas.paymentsapp.pojo.Payment;
import com.github.bvaidas.paymentsapp.repository.PaymentsRepository;
import com.github.bvaidas.paymentsapp.service.PaymentService;
import com.github.bvaidas.paymentsapp.service.exceptions.PaymentAccountNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/v1/payments/")
public class PaymentsApi {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PaymentsRepository paymentsRepository;

    @Autowired
    private PaymentService paymentService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public Page<Payment> list(Pageable pageable) {
        try {
            Page<Payment> result = paymentsRepository.findAll(pageable);
            return result;
        } catch (Exception e) {
            log.error("Failed to list payments", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to list payments", e);
        }
    }


    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Payment getPayment(@Valid @PathVariable("id") UUID paymentId) {
        log.info("Received payment get request : ID {}", paymentId);

        Optional<Payment> payment;
        try {

            payment = paymentsRepository.findById(paymentId);

            if (!payment.isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Payment not found");
            }
        } catch (Exception e) {
            log.error("Failed to retrieve payment details", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve payment details", e);
        }

        return payment.get();

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Payment create(@Valid @RequestBody PaymentRequest paymentRequest) {
        log.info("Received payment creation request payload: {}", paymentRequest);

        Payment payment;

        try {
            payment = paymentService.createPayment(paymentRequest);

        } catch (PaymentAccountNotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.PRECONDITION_FAILED, "Payment account not found", e);
        } catch (Exception ex) {
            log.error("Failed to make Payment", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to make a payment", ex);
        }
        return payment;
    }


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void put(@Valid @PathVariable("id") UUID paymentId, @Valid @RequestBody Payment payment) {
        log.info("Received payment ID {} update request payload : {}. ", paymentId, payment);

        try {
            //Check payment IDs
            if (!payment.getId().equals(paymentId)) {
                log.info("Request Payment ID: {} is not equal to Payload ID {}", paymentId, payment.getId());
                throw new ResponseStatusException(
                        HttpStatus.PRECONDITION_FAILED, "Payment IDs on payload and path parameters are not the same");
            }
            paymentsRepository.save(payment);
        } catch (Exception e) {
            log.error("Failed to update Payment", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to update payment", e);
        }

    }

    @PatchMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<UUID> bulkImport(@Valid @RequestBody List<Payment> list) {

        try {
            Iterable<Payment> payments = paymentsRepository.saveAll(list);
            List<UUID> paymentIds = StreamSupport.stream(payments.spliterator(), false).map(Payment::getId).collect(Collectors.toList());
            return paymentIds;
        } catch (Exception ex) {
            log.error("Failed to update payments", ex);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Failed to import payments", ex);
        }
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    public void delete(@Valid @PathVariable("id") UUID paymentId) {
        log.info("Received payment delete request : {}", paymentId);

        try {
            //Find payment by ID
            Optional<Payment> payment = paymentsRepository.findById(paymentId);

            //Check payment existence
            if (!payment.isPresent()) {
                throw new ResponseStatusException(
                        HttpStatus.PRECONDITION_FAILED, "Payment does not exist");
            }
            paymentsRepository.deleteById(paymentId);
        } catch (Exception e) {
            log.error("Failed to delete payment", e);
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, "Payment deletion failed", e);
        }
    }

}
