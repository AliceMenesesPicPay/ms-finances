package com.picpay.finances.util;

import com.picpay.finances.core.domain.Account;
import com.picpay.finances.core.domain.AccountType;
import com.picpay.finances.core.domain.Status;

import java.math.BigDecimal;

import static com.picpay.finances.core.domain.AccountType.CHECKING;
import static com.picpay.finances.util.HelpTest.AGENCY;
import static com.picpay.finances.util.HelpTest.ID;

public class AccountMock {

    public static Account create(AccountType accountType, Status status, BigDecimal balance) {
        return Account.builder()
                .id(ID)
                .number("12345")
                .digit("5")
                .agency(AGENCY)
                .balance(balance)
                .accountType(accountType)
                .status(status)
                .customerId(ID)
                .build();
    }

    public static Account createAccountData(String digit, String agency) {
        return Account.builder()
                .number("12345")
                .digit(digit)
                .agency(agency)
                .accountType(CHECKING)
                .build();
    }

}
