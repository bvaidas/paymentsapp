package com.github.bvaidas.paymentsapp.repository;

import com.github.bvaidas.paymentsapp.pojo.AccountInfo;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface AccountRepository extends PagingAndSortingRepository<AccountInfo, UUID> {
    public AccountInfo findOneByAccountNumberAndAccountNumberCodeAndName(String accountNumber, String accountNumberCode, String name);

}