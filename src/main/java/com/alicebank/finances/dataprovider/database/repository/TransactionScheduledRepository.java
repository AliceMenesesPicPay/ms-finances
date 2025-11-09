package com.alicebank.finances.dataprovider.database.repository;

import com.alicebank.finances.core.domain.TransactionScheduledStatus;
import com.alicebank.finances.dataprovider.database.entity.TransactionEntity;
import com.alicebank.finances.dataprovider.database.entity.TransactionScheduledEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface TransactionScheduledRepository extends JpaRepository<TransactionScheduledEntity, Long> {

    @Query("SELECT t FROM TransactionScheduledEntity t WHERE t.scheduledDate <= :scheduledDate AND t.updatedAt < :firstExecution AND t.status = :status")
    Page<TransactionScheduledEntity> findByScheduledDate(@Param("scheduledDate") LocalDate scheduledDate,
                                                          @Param("firstExecution") LocalDateTime firstExecution,
                                                          @Param("status") TransactionScheduledStatus status,
                                                          Pageable pageable);

}
