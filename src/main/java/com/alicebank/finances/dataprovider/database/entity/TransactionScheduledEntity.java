package com.alicebank.finances.dataprovider.database.entity;

import com.alicebank.finances.core.domain.TransactionScheduled;
import com.alicebank.finances.core.domain.TransactionScheduledStatus;
import com.alicebank.finances.core.domain.TransactionType;
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

import static com.alicebank.finances.dataprovider.database.entity.AccountEntity.fromAccount;

@Entity
@Table(name = "transactions_scheduled")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class TransactionScheduledEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountEntity fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private AccountEntity toAccount;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @OneToOne(fetch = FetchType.LAZY)
    private TransactionEntity transaction;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDate scheduledDate;

    @Enumerated(EnumType.STRING)
    private TransactionScheduledStatus status;

    public static TransactionScheduledEntity fromTransactionScheduled(TransactionScheduled transactionScheduled) {
        return TransactionScheduledEntity.builder()
                .id(transactionScheduled.getId())
                .fromAccount(fromAccount(transactionScheduled.getFromAccount()))
                .toAccount(fromAccount(transactionScheduled.getToAccount()))
                .transaction(transactionScheduled.getTransaction() != null ? TransactionEntity.fromTransaction(transactionScheduled.getTransaction()) : null)
                .amount(transactionScheduled.getAmount())
                .transactionType(transactionScheduled.getTransactionType())
                .createdAt(transactionScheduled.getCreatedAt())
                .updatedAt(transactionScheduled.getUpdatedAt())
                .scheduledDate(transactionScheduled.getScheduledDate())
                .status(transactionScheduled.getStatus())
                .build();
    }

    public TransactionScheduled toTransactionScheduled() {
        return TransactionScheduled.builder()
                .id(getId())
                .fromAccount(getFromAccount().toAccount())
                .toAccount(getToAccount().toAccount())
                .amount(getAmount())
                .transactionType(getTransactionType())
                .createdAt(getCreatedAt())
                .updatedAt(getUpdatedAt())
                .scheduledDate(scheduledDate)
                .status(status)
                .build();
    }

}
