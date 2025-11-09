package com.alicebank.finances.dataprovider.database.entity;

import com.alicebank.finances.core.domain.Account;
import com.alicebank.finances.core.domain.AccountType;
import com.alicebank.finances.core.domain.AccountStatus;
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
@Table(name = "accounts")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String number;
    private String digit;
    private String agency;
    private BigDecimal balance;
    private Long customerId;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Version
    private Integer version;

    public static AccountEntity fromAccount(final Account account) {
        return AccountEntity.builder()
                .id(account.getId())
                .number(account.getNumber())
                .digit(account.getDigit())
                .agency(account.getAgency())
                .balance(account.getBalance())
                .accountType(account.getAccountType())
                .status(account.getStatus())
                .customerId(account.getCustomerId())
                .version(account.getVersion())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }

    public Account toAccount() {
        return Account.builder()
                .id(id)
                .number(number)
                .digit(digit)
                .agency(agency)
                .balance(balance)
                .accountType(accountType)
                .status(status)
                .customerId(customerId)
                .version(version)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }

}
