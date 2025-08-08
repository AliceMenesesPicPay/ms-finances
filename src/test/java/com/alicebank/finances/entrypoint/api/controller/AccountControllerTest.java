package com.alicebank.finances.entrypoint.api.controller;

import com.alicebank.finances.core.usecase.AccountUseCase;
import com.alicebank.finances.entrypoint.api.controller.payload.response.AccountResponse;
import com.alicebank.finances.util.AccountMock;
import com.alicebank.finances.util.CustomerIdRequestMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.alicebank.finances.core.domain.AccountType.CHECKING;
import static com.alicebank.finances.core.domain.Status.ACTIVATED;
import static com.alicebank.finances.util.HelpTest.ID;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {

    @Mock
    private AccountUseCase accountUseCase;

    @InjectMocks
    private AccountController accountController;

    @Test
    void whenSearchByIdThenReturnAccount() {
        var account = AccountMock.create(CHECKING, ACTIVATED, ZERO);

        when(accountUseCase.searchById(ID)).thenReturn(account);

        var response = accountController.searchById(ID);

        assertThat(response)
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(AccountResponse.from(account));

        verify(accountUseCase).searchById(ID);
    }

    @Test
    void whenSearchByCustomerIdThenReturnAccounts() {
        var account1 = AccountMock.create(CHECKING, ACTIVATED, ZERO);
        var account2 = AccountMock.create(CHECKING, ACTIVATED, ZERO);

        when(accountUseCase.searchByCustomerId(ID)).thenReturn(List.of(account1, account2));

        var response = accountController.searchByCustomerId(ID);

        assertThat(response)
                .isNotNull()
                .hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        AccountResponse.from(account1),
                        AccountResponse.from(account2)
                );

        verify(accountUseCase).searchByCustomerId(ID);
    }

    @Test
    void whenCreateThenReturnAccounts() {
        var account1 = AccountMock.create(CHECKING, ACTIVATED, ZERO);
        var account2 = AccountMock.create(CHECKING, ACTIVATED, ZERO);

        when(accountUseCase.create(ID)).thenReturn(List.of(account1, account2));

        var response = accountController.create(CustomerIdRequestMock.create());

        assertThat(response)
                .isNotNull()
                .hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        AccountResponse.from(account1),
                        AccountResponse.from(account2)
                );

        verify(accountUseCase).create(ID);
    }

    @Test
    void whenCancelThenCancelAndReturnAccounts() {
        var account1 = AccountMock.create(CHECKING, ACTIVATED, ZERO);
        var account2 = AccountMock.create(CHECKING, ACTIVATED, ZERO);

        when(accountUseCase.cancel(ID)).thenReturn(List.of(account1, account2));

        var response = accountController.cancel(CustomerIdRequestMock.create());

        assertThat(response)
                .isNotNull()
                .hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        AccountResponse.from(account1),
                        AccountResponse.from(account2)
                );

        verify(accountUseCase).cancel(ID);
    }

}