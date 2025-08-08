package com.alicebank.finances.entrypoint.api.controller.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.alicebank.finances.core.domain.Transaction;
import com.alicebank.finances.core.domain.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
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
                .fromAccountId(transaction.getFromAccount().getId())
                .toAccountId(transaction.getToAccount().getId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

}
