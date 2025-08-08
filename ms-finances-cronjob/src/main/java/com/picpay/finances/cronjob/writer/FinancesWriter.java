package com.picpay.finances.cronjob.writer;

import com.picpay.finances.core.domain.Transaction;
import com.picpay.finances.core.usecase.TransactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinancesWriter implements ItemWriter<Transaction>  {

    private final TransactionUseCase transactionUseCase;

    @Override
    public void write(Chunk<? extends Transaction> chunk) {
        var transactions = chunk.getItems().stream()
                .map(e -> (Transaction) e)
                .toList();

        transactions.forEach(transactionUseCase::saveTransfer);
    }

}
