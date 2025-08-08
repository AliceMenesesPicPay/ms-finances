package com.alicebank.finances.core.gateway;

import com.alicebank.finances.core.domain.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionGateway {

    Optional<Transaction> findById(Long id);
    Transaction save(Transaction transaction);
    List<Transaction> findByCustomerId(Long customerId);

}
