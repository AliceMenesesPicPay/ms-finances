package com.picpay.finances.entrypoint.api.controller.payload.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class DepositRequest {

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    @Valid
    private AccountRequest account;

}
