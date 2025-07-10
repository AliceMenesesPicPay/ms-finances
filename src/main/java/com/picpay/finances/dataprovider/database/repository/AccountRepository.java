package com.picpay.finances.dataprovider.database.repository;

import com.picpay.finances.dataprovider.database.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<AccountEntity, Long> {

    List<AccountEntity> findByCustomerId(Long customerId);

}

