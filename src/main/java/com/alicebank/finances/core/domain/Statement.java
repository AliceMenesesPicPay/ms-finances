package com.alicebank.finances.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class Statement {

    private List<FinancialTransaction> financialTransactions;
    private LocalDateTime createdAt;

    public static Statement create(final List<FinancialTransaction> financialTransactions) {
        return Statement.builder()
                .financialTransactions(financialTransactions)
                .createdAt(LocalDateTime.now())
                .build();
    }

}
