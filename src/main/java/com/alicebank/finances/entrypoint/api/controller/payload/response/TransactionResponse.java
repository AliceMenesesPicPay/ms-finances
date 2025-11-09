package com.alicebank.finances.entrypoint.api.controller.payload.response;

import com.alicebank.finances.core.domain.Transaction;
import com.alicebank.finances.core.domain.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@Getter
public class TransactionResponse {

    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDateTime createdAt;

    public static TransactionResponse from(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .fromAccountId(transaction.getFromAccount() != null ? transaction.getFromAccount().getId() : null)
                .toAccountId(transaction.getToAccount().getId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

}
