package com.alicebank.finances.core.exception;

public class AccountNumberDuplicateException extends RuntimeException {

    public AccountNumberDuplicateException(Throwable ex) {
        super("An account with this number already exists.", ex);
    }

}
