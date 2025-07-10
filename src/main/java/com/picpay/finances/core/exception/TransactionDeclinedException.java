package com.picpay.finances.core.exception;

public class TransactionDeclinedException extends BusinessException {

    public TransactionDeclinedException(Long id) {
        super(String.format("Account with ID %d has insufficient balance for the transfer.", id));
    }

}
