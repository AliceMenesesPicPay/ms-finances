package com.alicebank.finances.core.usecase;

import com.alicebank.finances.core.domain.Statement;
import com.alicebank.finances.core.gateway.FinancialTransactionGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class StatementUseCase {

    private final FinancialTransactionGateway financialTransactionGateway;

    public Statement searchByCustomerId(final Long customerId, final Pageable pageable) {
        var financialTransactions = financialTransactionGateway.findByCustomerId(customerId, pageable);
        return Statement.create(financialTransactions);
    }

}
