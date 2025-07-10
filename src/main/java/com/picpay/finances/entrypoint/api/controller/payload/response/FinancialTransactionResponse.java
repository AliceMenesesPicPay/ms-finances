package com.picpay.finances.entrypoint.api.controller.payload.response;

import com.picpay.finances.core.domain.FinancialTransaction;
import com.picpay.finances.core.domain.FinancialTransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class FinancialTransactionResponse {

    private Long id;
    private FinancialTransactionType financialTransactionType;
    private BigDecimal amount;
    private Long accountId;
    private LocalDateTime createdAt;

    public static FinancialTransactionResponse from(FinancialTransaction financialTransaction) {
        return FinancialTransactionResponse.builder()
                .id(financialTransaction.getId())
                .financialTransactionType(financialTransaction.getFinancialTransactionType())
                .amount(financialTransaction.getAmount())
                .accountId(financialTransaction.getAccount().getId())
                .createdAt(financialTransaction.getCreatedAt())
                .build();
    }

}
