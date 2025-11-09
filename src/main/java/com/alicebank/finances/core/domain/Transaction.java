package com.alicebank.finances.core.domain;

import com.alicebank.finances.core.exception.AccountDuplicateException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.alicebank.finances.core.domain.TransactionType.*;

@Getter
@Builder
@Setter
public class Transaction implements ValidateTransaction {

    private Long id;
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;
    private TransactionType transactionType;
    private Integer version;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void transfer(final Account fromAccount, final Account toAccount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        transactionType = TRANSFER;

        getFromAccount().debit(amount);
        getToAccount().credit(amount);
    }

    public Transaction refund() {
        fromAccount.credit(amount);
        toAccount.debit(amount);

        return Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(amount)
                .transactionType(REFUND)
                .build();
    }

    @Override
    public void validateAccounts() {
        fromAccount.validate();
        toAccount.validate();
    }

    @Override
    public void areSameAccounts() {
        if (fromAccount.getNumber().equals(toAccount.getNumber())) {
            throw new AccountDuplicateException();
        }
    }

    public void deposit(final Account depositAccount) {
        this.toAccount = depositAccount;
        transactionType = DEPOSIT;

        getToAccount().credit(amount);
    }

}
