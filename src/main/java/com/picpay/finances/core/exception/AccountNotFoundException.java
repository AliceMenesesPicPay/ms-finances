package com.picpay.finances.core.exception;

public class AccountNotFoundException extends EntityNotFoundException {

    public AccountNotFoundException() {
        super("There isn't this account");
    }

}
