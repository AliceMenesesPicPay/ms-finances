package com.picpay.finances.dataprovider.database.impl;

import com.picpay.finances.core.domain.Transaction;
import com.picpay.finances.core.gateway.TransactionGateway;
import com.picpay.finances.dataprovider.database.entity.TransactionEntity;
import com.picpay.finances.dataprovider.database.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.picpay.finances.dataprovider.database.entity.TransactionEntity.fromTransaction;

@Service
@RequiredArgsConstructor
public class TransactionImpl implements TransactionGateway {

    private final TransactionRepository transactionRepository;

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.findById(id).map(TransactionEntity::toTransaction);
    }

    @Override
    public List<Transaction> findByCustomerId(Long customerId) {
        return transactionRepository.findByCustomerId(customerId)
                .stream()
                .map(TransactionEntity::toTransaction)
                .toList();
    }

    @Override
    public Transaction save(final Transaction transaction) {
        return transactionRepository.save(fromTransaction(transaction)).toTransaction();
    }

}
