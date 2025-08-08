package com.alicebank.finances.dataprovider.database.repository;

import com.alicebank.finances.dataprovider.database.entity.FinancialTransactionEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FinancialTransactionRepository extends JpaRepository<FinancialTransactionEntity, Long> {

    @Query("SELECT f FROM FinancialTransactionEntity f WHERE f.account.customerId = :customerId ORDER BY f.createdAt DESC")
    List<FinancialTransactionEntity> findByCustomerId(Long customerId, Pageable pageable);
//TODO criar transacao do tipo deposito
}

