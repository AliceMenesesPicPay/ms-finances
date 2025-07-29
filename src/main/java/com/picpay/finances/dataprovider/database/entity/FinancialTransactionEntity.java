package com.picpay.finances.dataprovider.database.entity;

import com.picpay.finances.core.domain.FinancialTransaction;
import com.picpay.finances.core.domain.FinancialTransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_transactions")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class FinancialTransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;
    private BigDecimal amount;
    private BigDecimal balance;

    @Enumerated(EnumType.STRING)
    private FinancialTransactionType financialTransactionType;

    @ManyToOne
    private AccountEntity account;

    @ManyToOne
    private TransactionEntity transaction;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static FinancialTransactionEntity fromFinancialTransaction(FinancialTransaction financialTransaction) {
        return FinancialTransactionEntity.builder()
                .id(financialTransaction.getId())
                .description(financialTransaction.getDescription())
                .amount(financialTransaction.getAmount())
                .balance(financialTransaction.getBalance())
                .financialTransactionType(financialTransaction.getFinancialTransactionType())
                .account(AccountEntity.fromAccount(financialTransaction.getAccount()))
                .transaction(financialTransaction.getTransaction() != null ? TransactionEntity.fromTransaction(financialTransaction.getTransaction()) : null)
                .build();
    }

    public FinancialTransaction toFinancialTransaction() {
        return FinancialTransaction.builder()
                .id(id)
                .description(description)
                .amount(amount)
                .balance(balance)
                .financialTransactionType(financialTransactionType)
                .account(account.toAccount())
                .transaction(transaction != null ? transaction.toTransaction() : null)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

}
