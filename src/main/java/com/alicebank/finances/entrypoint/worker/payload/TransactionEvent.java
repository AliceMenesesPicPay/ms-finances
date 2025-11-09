package com.alicebank.finances.entrypoint.worker.payload;

import com.alicebank.finances.core.domain.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransactionEvent {

    private AccountEvent fromAccount;
    private AccountEvent toAccount;
    private BigDecimal amount;

    public Transaction toTransaction() {
        return Transaction.builder()
                .fromAccount(fromAccount.toAccount())
                .toAccount(toAccount.toAccount())
                .amount(amount)
                .build();
    }

}
