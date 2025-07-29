package com.picpay.finances.util;

import com.picpay.finances.entrypoint.api.controller.payload.request.TransactionRequest;

import static com.picpay.finances.util.HelpTest.ID;
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
