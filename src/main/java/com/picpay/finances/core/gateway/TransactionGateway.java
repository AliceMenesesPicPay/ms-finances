package com.picpay.finances.core.gateway;

import com.picpay.finances.core.domain.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionGateway {

    Optional<Transaction> findById(Long id);
    Transaction save(Transaction transaction);
    List<Transaction> findByCustomerId(Long customerId);

}
