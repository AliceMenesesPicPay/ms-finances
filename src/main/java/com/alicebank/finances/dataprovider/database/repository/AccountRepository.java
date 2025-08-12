package com.alicebank.finances.dataprovider.database.repository;

import com.alicebank.finances.core.domain.AccountType;
import com.alicebank.finances.dataprovider.database.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    List<AccountEntity> findByCustomerId(Long customerId);

    Optional<AccountEntity> findByAccountTypeAndNumberAndDigit(AccountType accountType, String number, String digit);

    List<AccountEntity> findByAccountType(AccountType accountType);

}

