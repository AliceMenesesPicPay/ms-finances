package com.alicebank.finances.core.exception;

public class AccountConflictException extends ConflictException {

    public AccountConflictException() {
        super("Unable to update the account, please try again later.");
    }

}
