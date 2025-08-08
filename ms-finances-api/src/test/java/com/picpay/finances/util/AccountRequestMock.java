package com.picpay.finances.util;

import com.picpay.finances.entrypoint.api.controller.payload.request.AccountRequest;

import static com.picpay.finances.util.HelpTest.*;

public class AccountRequestMock {

    public static AccountRequest create() {
        return AccountRequest.builder()
                .number(NUMBER)
                .digit(DIGIT)
                .agency(AGENCY)
                .build();
    }

}
