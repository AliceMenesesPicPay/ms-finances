package com.alicebank.finances.entrypoint.api.controller.payload.response;

import com.alicebank.finances.core.domain.Statement;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public class StatementResponse {

    private List<FinancialTransactionResponse> financialTransactions;
    private LocalDateTime createdAt;

    public static StatementResponse from(Statement statement) {
        return StatementResponse.builder()
                .financialTransactions(statement.getFinancialTransactions().stream()
                        .map(FinancialTransactionResponse::from)
                        .toList())
                .createdAt(statement.getCreatedAt())
                .build();
    }

}
