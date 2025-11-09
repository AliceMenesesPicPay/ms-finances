package com.alicebank.finances.core.gateway;

import com.alicebank.finances.core.domain.FinancialTransaction;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FinancialTransactionGateway {

    List<FinancialTransaction> findByCustomerId(Long customerId, Pageable pageable);
    void saveAll(List<FinancialTransaction> financialTransactions);
    void save(FinancialTransaction financialTransaction);

}
