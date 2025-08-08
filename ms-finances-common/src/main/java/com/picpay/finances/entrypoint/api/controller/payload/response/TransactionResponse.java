package com.picpay.finances.entrypoint.api.controller.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.picpay.finances.core.domain.Transaction;
import com.picpay.finances.core.domain.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
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
    private LocalDate scheduledDate;
    private LocalDateTime createdAt;

    public static TransactionResponse from(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .fromAccountId(transaction.getFromAccount() != null ? transaction.getFromAccount().getId() : null)
                .toAccountId(transaction.getToAccount().getId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .scheduledDate(transaction.getScheduledDate())
                .createdAt(transaction.getCreatedAt())
                .build();
    }

}
