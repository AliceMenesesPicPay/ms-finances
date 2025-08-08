package com.picpay.finances.entrypoint.api.controller.payload.response;

import com.picpay.finances.core.domain.Account;
import com.picpay.finances.core.domain.AccountType;
import com.picpay.finances.core.domain.Status;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class AccountResponse {

    private Long id;
    private String number;
    private String digit;
    private String agency;
    private BigDecimal balance;
    private AccountType accountType;
    private Status status;
    private Long customerId;

    public static AccountResponse from(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .number(account.getNumber())
                .digit(account.getDigit())
                .agency(account.getAgency())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .customerId(account.getCustomerId())
                .build();
    }

}
