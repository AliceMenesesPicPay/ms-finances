package com.alicebank.finances.core.exception;

public class AccountDuplicateException extends BusinessException {

    public AccountDuplicateException() {
        super("Transfer to the same account is not allowed.");
    }

}
