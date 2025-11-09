package com.alicebank.finances.util;

import com.alicebank.finances.entrypoint.api.controller.payload.request.CustomerIdRequest;

import static com.alicebank.finances.util.HelpTest.ID;

public class CustomerIdRequestMock {

    public static CustomerIdRequest create() {
        return CustomerIdRequest.builder()
                .customerId(ID)
                .build();
    }

}
