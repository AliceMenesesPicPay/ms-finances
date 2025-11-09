package com.alicebank.finances.util;

import com.alicebank.finances.entrypoint.api.controller.payload.request.TransactionRequest;

import static java.math.BigDecimal.TEN;

public class TransactionRequestMock {

    public static TransactionRequest create() {
        return TransactionRequest.builder()
                .fromAccount(AccountRequestMock.create())
                .toAccount(AccountRequestMock.create())
                .amount(TEN)
                .build();
    }

}
