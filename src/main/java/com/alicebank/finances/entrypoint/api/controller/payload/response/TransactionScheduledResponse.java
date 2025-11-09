package com.alicebank.finances.entrypoint.api.controller.payload.response;

import com.alicebank.finances.core.domain.Transaction;
import com.alicebank.finances.core.domain.TransactionScheduled;
import com.alicebank.finances.core.domain.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class TransactionScheduledResponse {

    private Long id;
    private Long fromAccountId;
    private Long toAccountId;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDate scheduledDate;
    private LocalDateTime createdAt;

    public static TransactionScheduledResponse from(TransactionScheduled transactionScheduled) {
        return TransactionScheduledResponse.builder()
                .id(transactionScheduled.getId())
                .fromAccountId(transactionScheduled.getFromAccount().getId())
                .toAccountId(transactionScheduled.getToAccount().getId())
                .amount(transactionScheduled.getAmount())
                .transactionType(transactionScheduled.getTransactionType())
                .scheduledDate(transactionScheduled.getScheduledDate())
                .createdAt(transactionScheduled.getCreatedAt())
                .build();
    }

}
