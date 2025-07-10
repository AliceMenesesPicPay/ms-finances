package com.picpay.finances.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.picpay.finances.core.domain.TransactionType.REFUND;
import static com.picpay.finances.core.domain.TransactionType.TRANSFER;

@Getter
@Builder
public class Transaction {

    private Long id;
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;
    private TransactionType transactionType;
    private FinancialTransaction financialTransactionOrigin;
    private FinancialTransaction financialTransactionDestination;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Transaction newTransfer(final Account fromAccount, final Account toAccount, final BigDecimal amount) {
        var financialTransactionOrigin = FinancialTransaction.newDebit(fromAccount, amount);
        var financialTransactionDestination = FinancialTransaction.newCredit(toAccount, amount);

        return Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(amount)
                .transactionType(TRANSFER)
                .financialTransactionOrigin(financialTransactionOrigin)
                .financialTransactionDestination(financialTransactionDestination)
                .build();
    }

    public Transaction newRefund() {
        fromAccount.credit(amount);
        toAccount.debit(amount);

        return Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(amount)
                .transactionType(REFUND)
                .build();
    }

}
