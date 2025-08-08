package com.picpay.finances.core.exception;

public class AccountAlreadyCancelledException extends BusinessException {

    public AccountAlreadyCancelledException(Long id) {
        super(String.format("The account with ID %d has already been cancelled.", id));
    }

}
