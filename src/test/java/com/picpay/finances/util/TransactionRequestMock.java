package com.picpay.finances.util;

import com.picpay.finances.entrypoint.api.controller.payload.request.TransactionRequest;

import static com.picpay.finances.util.HelpTest.ID;
import static java.math.BigDecimal.TEN;

public class TransactionRequestMock {

    public static TransactionRequest create() {
        return TransactionRequest.builder()
                .fromAccountId(ID)
                .toAccountId(ID)
                .amount(TEN)
                .build();
    }

}
