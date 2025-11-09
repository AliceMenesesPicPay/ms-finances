package com.alicebank.finances.entrypoint.cronjob.reader;

import com.alicebank.finances.core.domain.TransactionScheduled;
import com.alicebank.finances.core.gateway.TransactionScheduledGateway;
import com.alicebank.finances.entrypoint.cronjob.config.FinancesJobConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.annotation.BeforeChunk;
import org.springframework.batch.core.annotation.BeforeRead;
import org.springframework.batch.core.annotation.BeforeStep;
import org.springframework.batch.item.ItemReader;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class FinancesReader implements ItemReader<TransactionScheduled>  {

    private final TransactionScheduledGateway transactionScheduledGateway;
    private final FinancesJobConfigProperties financesJobConfigProperties;
    private final ReaderControl readerControl;

    @Override
    public TransactionScheduled read() {
        return !readerControl.getCurrentTransactionsList().isEmpty() ? readerControl.getCurrentTransactionsList().removeFirst() : null;
    }

    private void nextPage() {
        var pageable = PageRequest.of(readerControl.getPage(), financesJobConfigProperties.pageSize(), Sort.by(Sort.Direction.ASC, "createdAt", "id"));

        var pageTransactions = transactionScheduledGateway.findByScheduledDate(LocalDate.now(), readerControl.getFirstExecution(), pageable);

        readerControl.setLastPage(pageTransactions.isLast());
        readerControl.setCurrentTransactionsList(new ArrayList<>(pageTransactions.getContent()));
    }

    @BeforeStep
    public void beforeStep() {
        readerControl.saveFirstExecution();
    }

    @BeforeChunk
    public void beforeChunk() {
        readerControl.restart();
    }

    @BeforeRead
    public void beforeRead() {
        if (readerControl.getCurrentTransactionsList().isEmpty() && !readerControl.isLastPage()) {
            nextPage();
            readerControl.sumCurrentPageNumber();
        }
    }

}