package com.alicebank.finances.dataprovider.database.impl;

import com.alicebank.finances.core.domain.TransactionScheduled;
import com.alicebank.finances.core.gateway.TransactionScheduledGateway;
import com.alicebank.finances.dataprovider.database.entity.TransactionScheduledEntity;
import com.alicebank.finances.dataprovider.database.repository.TransactionScheduledRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.alicebank.finances.core.domain.TransactionScheduledStatus.IN_PROGRESS;
import static com.alicebank.finances.dataprovider.database.entity.TransactionScheduledEntity.fromTransactionScheduled;

@Service
@RequiredArgsConstructor
public class TransactionScheduledImpl implements TransactionScheduledGateway {

    private final TransactionScheduledRepository transactionScheduledRepository;

    @Override
    public TransactionScheduled save(final TransactionScheduled transactionScheduled) {
        return transactionScheduledRepository.save(fromTransactionScheduled(transactionScheduled)).toTransactionScheduled();
    }

    @Override
    public Page<TransactionScheduled> findByScheduledDate(LocalDate scheduledDate, LocalDateTime firstExecution, PageRequest pageable) {
        return transactionScheduledRepository.findByScheduledDate(scheduledDate, firstExecution, IN_PROGRESS, pageable)
                .map(TransactionScheduledEntity::toTransactionScheduled);
    }

    @Override
    public Optional<TransactionScheduled> findById(Long id) {
        return transactionScheduledRepository.findById(id).map(TransactionScheduledEntity::toTransactionScheduled);
    }

}
