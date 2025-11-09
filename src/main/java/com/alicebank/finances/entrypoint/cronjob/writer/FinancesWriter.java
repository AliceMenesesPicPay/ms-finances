package com.alicebank.finances.entrypoint.cronjob.writer;

import com.alicebank.finances.core.domain.TransactionScheduled;
import com.alicebank.finances.core.usecase.TransactionUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FinancesWriter implements ItemWriter<TransactionScheduled>  {

    private final TransactionUseCase transactionUseCase;

    @Override
    public void write(Chunk<? extends TransactionScheduled> chunk) {
        var transactionsScheduled = chunk.getItems().stream()
                .map(e -> (TransactionScheduled) e)
                .toList();

        for (TransactionScheduled transactionScheduled : transactionsScheduled) {
            var transaction = transactionScheduled.getAndCreateTransaction();
            transactionUseCase.transfer(transaction);
            transactionUseCase.saveTransferScheduled(transactionScheduled);
        }
    }

}
