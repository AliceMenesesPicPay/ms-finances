package com.picpay.finances.dataprovider.database.repository;

import com.picpay.finances.core.domain.AccountType;
import com.picpay.finances.dataprovider.database.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    List<AccountEntity> findByCustomerId(Long customerId);

    Optional<AccountEntity> findByAccountTypeAndNumberAndDigit(AccountType accountType, String number, String digit);

}

