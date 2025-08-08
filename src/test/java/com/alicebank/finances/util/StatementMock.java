package com.alicebank.finances.util;

import com.alicebank.finances.core.domain.Statement;

import java.time.LocalDateTime;
import java.util.List;

public class StatementMock {

    public static Statement create() {
        return Statement.builder()
                .financialTransactions(List.of(FinancialTransactionMock.create()))
                .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .build();
    }

}
