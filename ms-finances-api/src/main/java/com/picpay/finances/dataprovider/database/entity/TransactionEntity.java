package com.picpay.finances.dataprovider.database.entity;

import com.picpay.finances.core.domain.Transaction;
import com.picpay.finances.core.domain.TransactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    private LocalDate scheduledDate;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    private Integer version;

    public static TransactionEntity fromTransaction(final Transaction transaction) {
        return TransactionEntity.builder()
                .id(transaction.getId())
                .fromAccount(transaction.getFromAccount() != null ? AccountEntity.fromAccount(transaction.getFromAccount()) : null)
                .toAccount(AccountEntity.fromAccount(transaction.getToAccount()))
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .scheduledDate(transaction.getScheduledDate())
                .createdAt(transaction.getCreatedAt())
                .updatedAt(transaction.getUpdatedAt())
                .build();
    }

    public Transaction toTransaction() {
        return Transaction.builder()
                .id(id)
                .fromAccount(fromAccount != null ? fromAccount.toAccount() : null)
                .toAccount(toAccount.toAccount())
                .amount(amount)
                .transactionType(transactionType)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

}
