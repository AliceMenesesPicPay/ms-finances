package com.alicebank.finances.core.exception;

public class TransactionConflictException extends RuntimeException {

    public TransactionConflictException() {
        super("Unable to update the transaction, please try again later.");
    }

}
