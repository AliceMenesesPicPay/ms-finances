package com.picpay.finances.core.domain;

import com.picpay.finances.core.exception.AccountDuplicateException;
import lombok.Builder;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.picpay.finances.core.domain.TransactionType.*;

@Getter
@Builder
public class Transaction {

    private Long id;
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDate scheduledDate;
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

    public void validateAccounts() {
        fromAccount.validate();
        toAccount.validate();
    }

    public void deposit(final Account depositAccount) {
        this.toAccount = depositAccount;
        transactionType = DEPOSIT;

        getToAccount().credit(amount);
    }

    public void scheduledTransfer(final Account fromAccount, final Account toAccount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        transactionType = TRANSFER;
    }

    public void areSameAccounts() {
        if (fromAccount.getNumber().equals(toAccount.getNumber())) {
            throw new AccountDuplicateException();
        }
    }
}
