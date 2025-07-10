package com.picpay.finances.util;

import com.picpay.finances.core.domain.FinancialTransaction;
import com.picpay.finances.core.domain.FinancialTransactionType;

import java.math.BigDecimal;

import static com.picpay.finances.core.domain.AccountType.CHECKING;
import static com.picpay.finances.core.domain.Status.ACTIVATED;
import static com.picpay.finances.util.HelpTest.ID;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

public class FinancialTransactionMock {

    public static FinancialTransaction create(FinancialTransactionType financialTransactionType, BigDecimal balance) {
        return FinancialTransaction.builder()
                .id(ID)
                .financialTransactionType(financialTransactionType)
                .amount(TEN)
                .account(AccountMock.create(CHECKING, ACTIVATED, balance))
                .build();
    }

}
