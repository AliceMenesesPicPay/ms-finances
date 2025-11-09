package com.alicebank.finances.core.gateway;

import com.alicebank.finances.core.domain.Transaction;
import com.alicebank.finances.core.domain.TransactionScheduled;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface TransactionScheduledGateway {

    TransactionScheduled save(TransactionScheduled transactionScheduled);
    Page<TransactionScheduled> findByScheduledDate(LocalDate scheduledDate, LocalDateTime firstExecution, PageRequest pageable);
    Optional<TransactionScheduled> findById(Long id);

}
