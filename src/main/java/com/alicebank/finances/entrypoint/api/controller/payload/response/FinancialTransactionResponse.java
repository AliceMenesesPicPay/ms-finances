package com.alicebank.finances.entrypoint.api.controller.payload.response;

import com.alicebank.finances.core.domain.FinancialTransaction;
import com.alicebank.finances.core.domain.FinancialTransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class FinancialTransactionResponse {

    private Long id;
    private String description;
    private BigDecimal amount;
    private BigDecimal balance;
    private FinancialTransactionType financialTransactionType;
    private LocalDateTime createdAt;

    public static FinancialTransactionResponse from(FinancialTransaction financialTransaction) {
        return FinancialTransactionResponse.builder()
                .id(financialTransaction.getId())
                .description(financialTransaction.getDescription())
                .amount(financialTransaction.getAmount())
                .balance(financialTransaction.getBalance())
                .createdAt(financialTransaction.getCreatedAt())
                .financialTransactionType(financialTransaction.getFinancialTransactionType())
                .build();
    }

}
