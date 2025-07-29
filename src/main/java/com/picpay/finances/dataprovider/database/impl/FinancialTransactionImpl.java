package com.picpay.finances.dataprovider.database.impl;

import com.picpay.finances.core.domain.FinancialTransaction;
import com.picpay.finances.core.gateway.FinancialTransactionGateway;
import com.picpay.finances.dataprovider.database.entity.FinancialTransactionEntity;
import com.picpay.finances.dataprovider.database.repository.FinancialTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FinancialTransactionImpl implements FinancialTransactionGateway {

    private final FinancialTransactionRepository financialTransactionRepository;

    @Override
    public List<FinancialTransaction> findByCustomerId(final Long customerId, final Pageable pageable) {
        return financialTransactionRepository.findByCustomerId(customerId, pageable).stream()
                .map(FinancialTransactionEntity::toFinancialTransaction)
                .toList();
    }

    @Override
    public void saveAll(final List<FinancialTransaction> financialTransactions) {
        financialTransactionRepository.saveAll(
                financialTransactions.stream()
                        .map(FinancialTransactionEntity::fromFinancialTransaction)
                        .toList()
        );
    }

    @Override
    public void save(final FinancialTransaction financialTransaction) {
        financialTransactionRepository.save(FinancialTransactionEntity.fromFinancialTransaction(financialTransaction));
    }

}
