package com.alicebank.finances.dataprovider.database.entity;

import com.alicebank.finances.core.domain.Transaction;
import com.alicebank.finances.core.domain.TransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AccountEntity fromAccount;

    @ManyToOne
    private AccountEntity toAccount;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static TransactionEntity fromTransaction(final Transaction transaction) {
        return TransactionEntity.builder()
                .id(transaction.getId())
                .fromAccount(AccountEntity.fromAccount(transaction.getFromAccount()))
                .toAccount(AccountEntity.fromAccount(transaction.getToAccount()))
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }

    public Transaction toTransaction() {
        return Transaction.builder()
                .id(id)
                .fromAccount(fromAccount.toAccount())
                .toAccount(toAccount.toAccount())
                .amount(amount)
                .transactionType(transactionType)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

}
