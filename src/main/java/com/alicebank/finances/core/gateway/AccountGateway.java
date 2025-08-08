package com.alicebank.finances.core.gateway;

import com.alicebank.finances.core.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountGateway {

    Optional<Account> findById(Long id);
    Optional<Account> searchByAccountCheckingAndNumberAndDigitAndAgency(Account account);
    List<Account> findByCustomerId(Long customerId);
    List<Account> saveAll(List<Account> accounts);
    void save(Account account);

}
