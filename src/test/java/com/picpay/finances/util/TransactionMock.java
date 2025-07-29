package com.picpay.finances.util;

import com.picpay.finances.core.domain.Status;
import com.picpay.finances.core.domain.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.picpay.finances.core.domain.AccountType.CHECKING;
import static com.picpay.finances.core.domain.Status.ACTIVATED;
import static com.picpay.finances.core.domain.TransactionType.REFUND;
import static com.picpay.finances.core.domain.TransactionType.TRANSFER;
import static com.picpay.finances.util.HelpTest.ID;
import static java.math.BigDecimal.TEN;
import static java.math.BigDecimal.ZERO;

public class TransactionMock {

    public static Transaction createTransfer() {
        return Transaction.builder()
                .id(ID)
                .fromAccount(AccountMock.create(CHECKING, ACTIVATED, TEN))
                .toAccount(AccountMock.create(CHECKING, ACTIVATED, TEN))
                .amount(TEN)
                .transactionType(TRANSFER)
                .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .build();
    }

    public static Transaction createRefund() {
        return Transaction.builder()
                .id(ID)
                .fromAccount(AccountMock.create(CHECKING, ACTIVATED, new BigDecimal(20)))
                .toAccount(AccountMock.create(CHECKING, ACTIVATED, ZERO))
                .amount(TEN)
                .transactionType(REFUND)
                .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .build();
    }

    public static Transaction createWithFromAccountBalance(BigDecimal balanceFromAccount) {
        return Transaction.builder()
                .id(ID)
                .fromAccount(AccountMock.create(CHECKING, ACTIVATED, balanceFromAccount))
                .toAccount(AccountMock.create(CHECKING, ACTIVATED, TEN))
                .amount(TEN)
                .transactionType(TRANSFER)
                .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .build();
    }

    public static Transaction createWithToAccountBalance(BigDecimal balanceToAccount) {
        return Transaction.builder()
                .id(ID)
                .fromAccount(AccountMock.create(CHECKING, ACTIVATED, TEN))
                .toAccount(AccountMock.create(CHECKING, ACTIVATED, balanceToAccount))
                .amount(TEN)
                .transactionType(TRANSFER)
                .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .build();
    }

    public static Transaction createWithAccountIsCanceled(Status statusFromAccount, Status statusToAccount) {
        return Transaction.builder()
                .id(ID)
                .fromAccount(AccountMock.create(CHECKING, statusFromAccount, TEN))
                .toAccount(AccountMock.create(CHECKING, statusToAccount, TEN))
                .amount(TEN)
                .transactionType(TRANSFER)
                .createdAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .updatedAt(LocalDateTime.of(2025, 1, 1, 0, 0, 0))
                .build();
    }

    public static Transaction createTransferData(String digit, String agency) {
        return Transaction.builder()
                .fromAccount(AccountMock.createAccountData(digit, agency))
                .toAccount(AccountMock.createAccountData(digit, digit))
                .amount(TEN)
                .build();
    }

}
