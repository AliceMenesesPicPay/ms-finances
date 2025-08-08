package com.alicebank.finances.core.usecase;

import com.alicebank.finances.core.domain.Account;
import com.alicebank.finances.core.domain.AccountType;
import com.alicebank.finances.core.domain.FinancialTransaction;
import com.alicebank.finances.core.exception.AccountNotFoundException;
import com.alicebank.finances.core.gateway.AccountGateway;
import com.alicebank.finances.core.gateway.FinancialTransactionGateway;
import com.alicebank.finances.util.AccountMock;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.alicebank.finances.core.domain.AccountType.CHECKING;
import static com.alicebank.finances.core.domain.AccountType.SAVINGS;
import static com.alicebank.finances.core.domain.Status.ACTIVATED;
import static com.alicebank.finances.core.domain.Status.CANCELED;
import static com.alicebank.finances.util.HelpTest.AGENCY;
import static com.alicebank.finances.util.HelpTest.ID;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AccountUseCaseTest {

    @Mock
    private AccountGateway accountGateway;

    @Mock
    private FinancialTransactionGateway financialTransactionGateway;

    @InjectMocks
    private AccountUseCase accountUseCase;

    @Captor
    private ArgumentCaptor<List<Account>> accountsCaptor;

    @Captor
    private ArgumentCaptor<FinancialTransaction> financialTransactionCaptor;

    @Nested
    class SearchByIdTest {

        @Test
        void whenSearchByIdThenReturnAccount() {
            var account = AccountMock.create(CHECKING, ACTIVATED, ZERO);

            when(accountGateway.findById(ID)).thenReturn(Optional.of(account));

            var result = accountUseCase.searchById(ID);

            assertThat(result).usingRecursiveComparison().isEqualTo(account);

            verify(accountGateway).findById(ID);
        }

        @Test
        void whenSearchByIdThenThrowsAccountNotFoundException() {
            when(accountGateway.findById(ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountUseCase.searchById(ID))
                    .isInstanceOf(AccountNotFoundException.class);

            verify(accountGateway).findById(ID);
        }

    }

    @Nested
    class SearchByCustomerIdTest {

        @Test
        void whenSearchByCustomerIdThenReturnAccounts() {
            var account1 = AccountMock.create(CHECKING, ACTIVATED, ZERO);
            var account2 = AccountMock.create(SAVINGS, ACTIVATED, ZERO);

            when(accountGateway.findByCustomerId(ID)).thenReturn(List.of(account1, account2));

            var result = accountUseCase.searchByCustomerId(ID);

            assertThat(result)
                    .isNotNull()
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(account1, account2);

            verify(accountGateway).findByCustomerId(ID);
        }

    }

    @Nested
    class CreateAccountTest {

        @Test
        void whenCreateAccountsThenCreateAndReturnAccounts() {
            var account1 = AccountMock.create(CHECKING, ACTIVATED, ZERO);
            var account2 = AccountMock.create(SAVINGS, ACTIVATED, ZERO);

            when(accountGateway.saveAll(anyList())).thenReturn(List.of(account1, account2));

            var result = accountUseCase.create(ID);

            verify(accountGateway).saveAll(accountsCaptor.capture());

            var accounts = accountsCaptor.getValue();

            assertThat(accounts)
                    .isNotEmpty();

            var accountChecking = accounts.get(0);
            var accountSavings = accounts.get(1);

            assertAccount(accountChecking, CHECKING);
            assertAccount(accountSavings, SAVINGS);

            assertThat(result)
                    .isNotNull()
                    .hasSize(2)
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(account1, account2);
        }

        private void assertAccount(Account account, AccountType type) {
            assertThat(account).isNotNull();
            assertThat(account.getAccountType()).isEqualTo(type);
            assertThat(account.getStatus()).isEqualTo(ACTIVATED);
            assertThat(account.getBalance()).isEqualByComparingTo(ZERO);
            assertThat(account.getAgency()).isEqualTo(AGENCY);
            assertThat(account.getCustomerId()).isEqualTo(ID);
            assertThat(account.getNumber())
                    .isNotNull()
                    .hasSize(6);
            assertThat(account.getDigit()).isNotNull();
        }

    }

    @Nested
    class CancelAccountTest {

        @Test
        void whenCancelAccountsThenCancelAndReturnAccounts() {
            var account1 = AccountMock.create(CHECKING, ACTIVATED, ZERO);
            var account2 = AccountMock.create(SAVINGS, ACTIVATED, ZERO);
            var accounts = List.of(account1, account2);

            var canceledAccount1 = AccountMock.create(CHECKING, CANCELED, ZERO);
            var canceledAccount2 = AccountMock.create(SAVINGS, CANCELED, ZERO);
            var canceledAccounts = List.of(canceledAccount1, canceledAccount2);

            when(accountGateway.findByCustomerId(ID)).thenReturn(accounts);
            when(accountGateway.saveAll(accountsCaptor.capture())).thenReturn(canceledAccounts);

            var result = accountUseCase.cancel(ID);

            assertThat(result)
                    .isNotNull()
                    .usingRecursiveFieldByFieldElementComparator()
                    .isEqualTo(canceledAccounts);

            verify(accountGateway).findByCustomerId(ID);
            verify(accountGateway).saveAll(accounts);

            accounts = accountsCaptor.getValue();

            assertThat(accounts)
                    .isNotNull()
                    .usingRecursiveFieldByFieldElementComparator()
                    .isEqualTo(canceledAccounts);
        }

        @Test
        void whenCancelAccountsAndAccountsNotFoundThenThrowAccountNotFoundException() {
            when(accountGateway.findByCustomerId(ID)).thenReturn(List.of());

            assertThatThrownBy(() -> accountUseCase.cancel(ID))
                    .isInstanceOf(AccountNotFoundException.class);

            verify(accountGateway).findByCustomerId(ID);
            verify(accountGateway, never()).saveAll(anyList());
        }

    }

    @Nested
    class DepositTest {

        @Test
        void whenDepositThenAccountIsCreditedAndTransactionIsSaved() {
            var account = AccountMock.create(CHECKING, ACTIVATED, ZERO);
            var amount = new BigDecimal("100.00");

            when(accountGateway.searchByAccountCheckingAndNumberAndDigitAndAgency(any(Account.class)))
                    .thenReturn(Optional.of(account));

            accountUseCase.deposit(amount, account);

            verify(accountGateway).save(account);
            verify(financialTransactionGateway).save(financialTransactionCaptor.capture());

            var financialTransaction = financialTransactionCaptor.getValue();

            assertThat(account.getBalance()).isEqualByComparingTo(amount);
            assertThat(financialTransaction).usingRecursiveComparison().isEqualTo(FinancialTransaction.createDeposit(amount, account));
        }

        @Test
        void whenDepositWithNonExistentAccountThenThrowAccountNotFoundException() {
            var account = AccountMock.create(CHECKING, ACTIVATED, ZERO);
            var amount = new BigDecimal("100.00");

            when(accountGateway.searchByAccountCheckingAndNumberAndDigitAndAgency(any(Account.class)))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> accountUseCase.deposit(amount, account))
                    .isInstanceOf(AccountNotFoundException.class);

            verify(accountGateway, never()).save(any(Account.class));
            verify(financialTransactionGateway, never()).save(any(FinancialTransaction.class));
        }

        @Nested
        class SearchByAccountCheckingAndNumberAndDigitAndAgencyTest {

            @Test
            void whenAccountExistsShouldReturnAccount() {
                var account = AccountMock.create(CHECKING, ACTIVATED, ZERO);

                when(accountGateway.searchByAccountCheckingAndNumberAndDigitAndAgency(account))
                        .thenReturn(Optional.of(account));

                var result = accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(account);

                verify(accountGateway).searchByAccountCheckingAndNumberAndDigitAndAgency(account);

                assertThat(result).usingRecursiveComparison().isEqualTo(account);
            }

            @Test
            void whenAccountDoesNotExistShouldThrowAccountNotFoundException() {
                var account = AccountMock.create(CHECKING, ACTIVATED, ZERO);

                when(accountGateway.searchByAccountCheckingAndNumberAndDigitAndAgency(account))
                        .thenReturn(Optional.empty());

                assertThatThrownBy(() -> accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(account))
                        .isInstanceOf(AccountNotFoundException.class);

                verify(accountGateway).searchByAccountCheckingAndNumberAndDigitAndAgency(account);
            }

        }

    }

}