package com.alicebank.finances.core.gateway;

import com.alicebank.finances.core.domain.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionGateway {

    Optional<Transaction> findById(Long id);
    Transaction save(Transaction transaction);
    List<Transaction> findByCustomerId(Long customerId);

}
