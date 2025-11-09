package com.alicebank.finances.util;

import com.alicebank.finances.entrypoint.api.controller.payload.request.AccountRequest;

import static com.alicebank.finances.util.HelpTest.*;

public class AccountRequestMock {

    public static AccountRequest create() {
        return AccountRequest.builder()
                .number(NUMBER)
                .digit(DIGIT)
                .agency(AGENCY)
                .build();
    }

}
