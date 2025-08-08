package com.picpay.finances.cronjob.processor;

import com.picpay.finances.core.domain.Transaction;
import com.picpay.finances.core.usecase.TransactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinancesProcessor implements ItemProcessor<Transaction, Transaction> {

    private final TransactionUseCase transactionUseCase;

    @Override
    public Transaction process(Transaction transaction) {
        transactionUseCase.validateAndCreateTransfer(transaction);
        return transaction;
    }

}