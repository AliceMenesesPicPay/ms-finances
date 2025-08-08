package com.picpay.finances.cronjob.reader;

import com.picpay.finances.core.domain.Transaction;
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
    private List<Transaction> currentTransactionsList;
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