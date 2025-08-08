package com.picpay.finances.util;

import com.picpay.finances.entrypoint.api.controller.payload.request.CustomerIdRequest;

import static com.picpay.finances.util.HelpTest.ID;

public class CustomerIdRequestMock {

    public static CustomerIdRequest create() {
        return CustomerIdRequest.builder()
                .customerId(ID)
                .build();
    }

}
