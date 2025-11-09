package com.alicebank.finances.entrypoint.api.controller.payload.request;

import com.alicebank.finances.core.domain.Account;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AccountRequest {

    @NotBlank
    private String number;

    @NotBlank
    private String digit;

    @NotBlank
    private String agency;

    public Account toAccount() {
        return Account.builder()
                .number(number)
                .digit(digit)
                .agency(agency)
                .build();
    }

}
