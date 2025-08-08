package com.picpay.finances.util;

import com.picpay.finances.core.domain.FinancialTransaction;

import java.time.LocalDateTime;

import static com.picpay.finances.core.domain.AccountType.CHECKING;
import static com.picpay.finances.core.domain.Status.ACTIVATED;
import static com.picpay.finances.util.HelpTest.ID;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

public class FinancialTransactionMock {

    public static FinancialTransaction create() {
        return FinancialTransaction.builder()
                .id(ID)
                .amount(TEN)
                .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .build();
    }

    public static FinancialTransaction createWithAccount() {
        return FinancialTransaction.builder()
                .id(ID)
                .amount(TEN)
                .account(AccountMock.create(CHECKING, ACTIVATED, ZERO))
                .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .build();
    }

}
