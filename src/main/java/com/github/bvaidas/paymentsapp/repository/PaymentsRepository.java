package com.github.bvaidas.paymentsapp.repository;

import com.github.bvaidas.paymentsapp.pojo.Payment;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface PaymentsRepository extends PagingAndSortingRepository<Payment, UUID> {

    void deleteById(UUID id);

}
