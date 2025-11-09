package com.alicebank.finances.entrypoint.cronjob.reader;

import com.alicebank.finances.core.domain.Transaction;
import com.alicebank.finances.core.domain.TransactionScheduled;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
public class ReaderControl {

    private int page;
    private List<TransactionScheduled> currentTransactionsList;
    private boolean isLastPage;
    private LocalDateTime firstExecution;

    public void sumCurrentPageNumber() {
        page++;
    }

    public void restart() {
        page = 0;
        currentTransactionsList = new ArrayList<>();
        isLastPage = false;
    }

    public void saveFirstExecution() {
        firstExecution = LocalDateTime.now();
    }

}