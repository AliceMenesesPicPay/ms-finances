package com.picpay.finances.core.domain;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.picpay.finances.core.domain.FinancialTransactionType.DEBIT;

@Getter
@Builder
public class FinancialTransaction {

    private Long id;
    private FinancialTransactionType financialTransactionType;
    private BigDecimal amount;
    private Account account;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FinancialTransaction newDebit(final Account account, final BigDecimal amount) {
        account.debit(amount);

        return FinancialTransaction.builder()
                .financialTransactionType(DEBIT)
                .amount(amount)
                .account(account)
                .build();
    }

    public static FinancialTransaction newCredit(final Account account, final BigDecimal amount) {
        account.credit(amount);

        return FinancialTransaction.builder()
                .financialTransactionType(FinancialTransactionType.CREDIT)
                .amount(amount)
                .account(account)
                .build();
    }

}
