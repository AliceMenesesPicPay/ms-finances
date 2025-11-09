package com.alicebank.finances.core.exception;

public class TransactionNotFoundException extends EntityNotFoundException {

    public TransactionNotFoundException(Long id) {
        super(String.format("There isn't an transaction with ID %d", id));
    }

}
