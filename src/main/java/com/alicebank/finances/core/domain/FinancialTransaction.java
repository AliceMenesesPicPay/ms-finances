package com.alicebank.finances.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.alicebank.finances.core.domain.FinancialTransactionType.CREDIT;
import static com.alicebank.finances.core.domain.FinancialTransactionType.DEBIT;

@Getter
@Builder
public class FinancialTransaction {

    private Long id;
    private String description;
    private BigDecimal amount;
    private BigDecimal balance;
    private FinancialTransactionType financialTransactionType;
    private Account account;
    private Transaction transaction;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FinancialTransaction createTransferDebit(final Transaction transaction) {
        return FinancialTransaction.builder()
                .description(String.format("Payment to %s-%s", transaction.getFromAccount().getNumber(), transaction.getFromAccount().getDigit()))
                .amount(transaction.getAmount().negate())
                .balance(transaction.getFromAccount().getBalance())
                .financialTransactionType(DEBIT)
                .account(transaction.getFromAccount())
                .transaction(transaction)
                .build();
    }

    public static FinancialTransaction createTransferCredit(final Transaction transaction) {
        return FinancialTransaction.builder()
                .description(String.format("Payment from %s-%s", transaction.getToAccount().getNumber(), transaction.getToAccount().getDigit()))
                .amount(transaction.getAmount())
                .balance(transaction.getToAccount().getBalance())
                .financialTransactionType(CREDIT)
                .account(transaction.getToAccount())
                .transaction(transaction)
                .build();
    }

    public static FinancialTransaction createRefundDebit(final Transaction transaction) {
        return FinancialTransaction.builder()
                .description(String.format("Refund to %s-%s", transaction.getFromAccount().getNumber(), transaction.getFromAccount().getDigit()))
                .amount(transaction.getAmount().negate())
                .balance(transaction.getToAccount().getBalance())
                .financialTransactionType(DEBIT)
                .account(transaction.getToAccount())
                .transaction(transaction)
                .build();
    }

    public static FinancialTransaction createRefundCredit(final Transaction transaction) {
        return FinancialTransaction.builder()
                .description(String.format("Refund from %s-%s", transaction.getToAccount().getNumber(), transaction.getToAccount().getDigit()))
                .amount(transaction.getAmount())
                .balance(transaction.getFromAccount().getBalance())
                .financialTransactionType(CREDIT)
                .account(transaction.getFromAccount())
                .transaction(transaction)
                .build();
    }

    public static FinancialTransaction createDeposit(final BigDecimal amount, final Account depositAccount) {
        return FinancialTransaction.builder()
                .description(String.format("Deposited R$%s into the checking account", amount))
                .amount(amount)
                .balance(depositAccount.getBalance())
                .financialTransactionType(CREDIT)
                .account(depositAccount)
                .build();
    }

}
