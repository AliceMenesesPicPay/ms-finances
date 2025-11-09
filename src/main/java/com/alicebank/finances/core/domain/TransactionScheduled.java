package com.alicebank.finances.core.domain;

import com.alicebank.finances.core.exception.AccountDuplicateException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.alicebank.finances.core.domain.TransactionScheduledStatus.*;
import static com.alicebank.finances.core.domain.TransactionType.TRANSFER;

@Getter
@Setter
@Builder
public class TransactionScheduled implements ValidateTransaction {

    private Long id;
    private Account fromAccount;
    private Account toAccount;
    private Transaction transaction;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDate scheduledDate;
    private TransactionScheduledStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public void scheduledTransfer(final Account fromAccount, final Account toAccount) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        transactionType = TRANSFER;
        status = IN_PROGRESS;
    }

    public void cancel() {
        status = CANCELLED;
        transaction = null;
    }

    public void complete(Transaction transaction) {
        status = COMPLETED;
        this.transaction = transaction;
    }

    public void complete() {
        status = COMPLETED;
        this.toAccount = transaction.getToAccount();
        this.fromAccount = transaction.getFromAccount();
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

    public Transaction getAndCreateTransaction() {
        transaction = Transaction.builder()
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .amount(amount)
                .transactionType(transactionType)
                .build();

        return transaction;
    }

    public void updateAccounts(List<Account> accounts) {
        this.fromAccount = accounts.stream()
                .filter(account -> account.getId().equals(this.fromAccount.getId()))
                .findFirst()
//                .map(account -> Account.builder().id(account.getId()).version(account.getVersion()).build())
                .orElse(this.fromAccount);

        this.toAccount = accounts.stream()
                .filter(account -> account.getId().equals(this.toAccount.getId()))
                .findFirst()
//                .map(account -> Account.builder().id(account.getId()).version(account.getVersion()).build())
                .orElse(this.toAccount);


    }
}
