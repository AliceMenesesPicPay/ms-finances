package com.picpay.finances.core.gateway;

import com.picpay.finances.core.domain.Account;

import java.util.List;
import java.util.Optional;

public interface AccountGateway {

    Optional<Account> findById(Long id);
    List<Account> findByCustomerId(Long customerId);
    List<Account> saveAll(List<Account> accounts);

}
