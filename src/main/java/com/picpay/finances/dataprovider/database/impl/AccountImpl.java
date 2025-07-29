package com.picpay.finances.dataprovider.database.impl;

import com.picpay.finances.core.domain.Account;
import com.picpay.finances.core.domain.AccountType;
import com.picpay.finances.core.gateway.AccountGateway;
import com.picpay.finances.dataprovider.database.entity.AccountEntity;
import com.picpay.finances.dataprovider.database.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.picpay.finances.core.domain.AccountType.CHECKING;

@Service
@RequiredArgsConstructor
public class AccountImpl implements AccountGateway {

    private final AccountRepository accountRepository;

    @Override
    public Optional<Account> findById(final Long id) {
        return accountRepository.findById(id).map(AccountEntity::toAccount);
    }

    @Override
    public Optional<Account> searchByAccountCheckingAndNumberAndDigitAndAgency(final Account account) {
        return accountRepository.findByAccountTypeAndNumberAndDigit(CHECKING, account.getNumber(), account.getDigit())
                .map(AccountEntity::toAccount);
    }

    @Override
    public List<Account> findByCustomerId(final Long customerId) {
        var accountEntities = accountRepository.findByCustomerId(customerId);
        return accountEntities.stream().map(AccountEntity::toAccount).toList();
    }

    @Override
    public List<Account> saveAll(final List<Account> accounts) {
        var accountEntities = accounts.stream().map(AccountEntity::fromAccount).toList();
        accountEntities = accountRepository.saveAll(accountEntities);

        return accountEntities.stream().map(AccountEntity::toAccount).toList();
    }

    @Override
    public void save(final Account account) {
        accountRepository.save(AccountEntity.fromAccount(account));
    }

}
