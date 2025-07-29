package com.picpay.finances.entrypoint.api.controller.payload.request;

import com.picpay.finances.core.domain.Transaction;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class TransactionRequest {

    @NotNull
    @Valid
    private AccountRequest fromAccount;

    @NotNull
    @Valid
    private AccountRequest toAccount;

    @NotNull
    @Positive
    private BigDecimal amount;

    public Transaction toTransaction() {
        return Transaction.builder()
                .fromAccount(fromAccount.toAccount())
                .toAccount(toAccount.toAccount())
                .amount(amount)
                .build();
    }

}
