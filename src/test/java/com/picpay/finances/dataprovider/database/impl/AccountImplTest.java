package com.picpay.finances.dataprovider.database.impl;

import com.picpay.finances.dataprovider.database.entity.AccountEntity;
import com.picpay.finances.dataprovider.database.repository.AccountRepository;
import com.picpay.finances.util.AccountMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.picpay.finances.core.domain.AccountType.CHECKING;
import static com.picpay.finances.core.domain.Status.ACTIVATED;
import static com.picpay.finances.util.HelpTest.ID;
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

}