package com.picpay.finances.core.domain;

import com.picpay.finances.dataprovider.database.entity.FinancialTransactionEntity;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.picpay.finances.core.domain.TransactionType.REFUND;
import static com.picpay.finances.core.domain.TransactionType.TRANSFER;

@Getter
@Builder
public class Transaction {

    private Long id;
    private Account fromAccount;
    private Account toAccount;
    private BigDecimal amount;
    private TransactionType transactionType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    //TODO
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

}
