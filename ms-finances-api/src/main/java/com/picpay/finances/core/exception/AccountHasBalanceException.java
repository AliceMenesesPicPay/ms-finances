package com.picpay.finances.core.exception;

public class AccountHasBalanceException extends BusinessException {

    public AccountHasBalanceException(Long id) {
        super(String.format("The account with ID %d still has a balance.", id));
    }

}
