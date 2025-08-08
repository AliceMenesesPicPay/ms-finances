package com.alicebank.finances.dataprovider.database.impl;

import com.alicebank.finances.dataprovider.database.entity.AccountEntity;
import com.alicebank.finances.dataprovider.database.repository.AccountRepository;
import com.alicebank.finances.util.AccountMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.alicebank.finances.core.domain.AccountType.CHECKING;
import static com.alicebank.finances.core.domain.Status.ACTIVATED;
import static com.alicebank.finances.util.HelpTest.ID;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountImplTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountImpl accountImpl;

    @Test
    void whenFindByIdThenReturnAccount() {
        var account = AccountMock.create(CHECKING, ACTIVATED, ZERO);

        when(accountRepository.findById(ID)).thenReturn(Optional.of(AccountEntity.fromAccount(account)));

        var result = accountImpl.findById(ID);

        assertThat(result).isPresent();

        assertThat(result.get())
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(account);

        verify(accountRepository).findById(ID);
    }

    @Test
    void whenFindByCustomerIdThenReturnAccounts() {
        var account = AccountMock.create(CHECKING, ACTIVATED, ZERO);

        when(accountRepository.findByCustomerId(ID)).thenReturn(List.of(AccountEntity.fromAccount(account)));

        var result = accountImpl.findByCustomerId(ID);

        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(account);

        verify(accountRepository).findByCustomerId(ID);
    }

    @Test
    void whenSaveAllThenReturnAccounts() {
        var account = AccountMock.create(CHECKING, ACTIVATED, ZERO);
        var entity = AccountEntity.fromAccount(account);

        when(accountRepository.saveAll(anyList())).thenReturn(List.of(entity));

        var result = accountImpl.saveAll(List.of(account));

        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(account);

        verify(accountRepository).saveAll(anyList());
    }

    @Test
    void whenSearchByAccountCheckingAndNumberAndDigitAndAgencyThenReturnAccount() {
        var account = AccountMock.create(CHECKING, ACTIVATED, ZERO);
        var entity = AccountEntity.fromAccount(account);

        when(accountRepository.findByAccountTypeAndNumberAndDigit(
                CHECKING, account.getNumber(), account.getDigit()))
                .thenReturn(Optional.of(entity));

        var result = accountImpl.searchByAccountCheckingAndNumberAndDigitAndAgency(account);

        assertThat(result).isPresent();

        assertThat(result.get())
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(entity);

        verify(accountRepository).findByAccountTypeAndNumberAndDigit(
                CHECKING, account.getNumber(), account.getDigit());
    }

    @Test
    void whenSaveThenRepositorySaveIsCalled() {
        var account = AccountMock.create(CHECKING, ACTIVATED, ZERO);
        var entity = AccountEntity.fromAccount(account);

        when(accountRepository.save(any(AccountEntity.class))).thenReturn(entity);

        accountImpl.save(account);

        verify(accountRepository).save(any(AccountEntity.class));
    }

}