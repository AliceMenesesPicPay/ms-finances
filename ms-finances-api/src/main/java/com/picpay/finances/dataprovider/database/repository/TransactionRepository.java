package com.picpay.finances.dataprovider.database.repository;

import com.picpay.finances.dataprovider.database.entity.TransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

    @Query("SELECT t FROM TransactionEntity t WHERE t.fromAccount.customerId = :customerId OR t.toAccount.customerId = :customerId ORDER BY t.createdAt DESC")
    List<TransactionEntity> findByCustomerId(Long customerId);

    @Query("SELECT t FROM TransactionEntity t WHERE t.scheduledDate <= :scheduledDate AND t.updatedAt < :firstExecution")
    Page<TransactionEntity> findByScheduledDate(LocalDate scheduledDate, LocalDateTime firstExecution, Pageable pageable);
//TODO ter uma tabela de agendamentos
}

