package com.picpay.finances.dataprovider.database.entity;

import com.picpay.finances.core.domain.FinancialTransaction;
import com.picpay.finances.core.domain.FinancialTransactionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.picpay.finances.dataprovider.database.entity.AccountEntity.fromAccount;

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

    @Enumerated(EnumType.STRING)
    private FinancialTransactionType financialTransactionType;

    private BigDecimal amount;

    @ManyToOne
    private AccountEntity account;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;


    public static FinancialTransactionEntity fromFinancialTransaction(final FinancialTransaction financialTransaction) {
        return FinancialTransactionEntity.builder()
                .id(financialTransaction.getId())
                .financialTransactionType(financialTransaction.getFinancialTransactionType())
                .amount(financialTransaction.getAmount())
                .account(fromAccount(financialTransaction.getAccount()))
                .createdAt(financialTransaction.getCreatedAt())
                .updatedAt(financialTransaction.getUpdatedAt())
                .build();
    }

    public FinancialTransaction toFinancialTransaction() {
        return FinancialTransaction.builder()
                .id(id)
                .financialTransactionType(financialTransactionType)
                .amount(amount)
                .account(account.toAccount())
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

}
