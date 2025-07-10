package com.picpay.finances.core.usecase;

import com.picpay.finances.core.domain.*;
import com.picpay.finances.core.exception.*;
import com.picpay.finances.core.gateway.TransactionGateway;
import com.picpay.finances.util.TransactionMock;
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

import static com.picpay.finances.core.domain.Status.ACTIVATED;
import static com.picpay.finances.core.domain.Status.CANCELED;
import static com.picpay.finances.util.HelpTest.ID;
import static java.math.BigDecimal.*;
import static java.math.BigDecimal.ZERO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionUseCaseTest {

    @Mock
    private AccountUseCase accountUseCase;

    @Mock
    private TransactionGateway transactionGateway;

    @InjectMocks
    private TransactionUseCase transactionUseCase;

    @Captor
    private ArgumentCaptor<List<Account>> accountsCaptor;

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    @Nested
    class SearchByIdTest {

        @Test
        void whenSearchByIdThenReturnTransaction() {
            var transaction = TransactionMock.createTransfer();

            when(transactionGateway.findById(ID)).thenReturn(Optional.of(transaction));

            var result = transactionUseCase.searchById(ID);

            assertThat(result).usingRecursiveComparison().isEqualTo(transaction);

            verify(transactionGateway).findById(ID);
        }

        @Test
        void whenSearchByIdAndTransactionNotFoundThenThrowTransactionNotFoundException() {
            when(transactionGateway.findById(ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> transactionUseCase.searchById(1L))
                    .isInstanceOf(TransactionNotFoundException.class)
                    .hasMessageContaining(ID.toString());

            verify(transactionGateway).findById(ID);
        }

    }

    @Nested
    class SearchByCustomerIdTest {

        @Test
        void whenSearchByCustomerIdThenReturnTransactions() {
            var transaction = TransactionMock.createTransfer();

            when(transactionGateway.findByCustomerId(ID)).thenReturn(List.of(transaction));

            var result = transactionUseCase.searchByCustomerId(ID);

            assertThat(result)
                    .isNotNull()
                    .usingRecursiveFieldByFieldElementComparator()
                    .containsExactly(transaction);

            verify(transactionGateway).findByCustomerId(ID);
        }

    }

    @Nested
    class CreateTransferTest {

        @Test
        void whenCreateTransferThenReturnTransaction() {
            var transaction = TransactionMock.createTransfer();

            when(accountUseCase.searchById(ID))
                    .thenReturn(transaction.getFromAccount())
                    .thenReturn(transaction.getToAccount());

            when(transactionGateway.save(transactionCaptor.capture())).thenReturn(transaction);

            var result = transactionUseCase.createTransfer(ID, ID, TEN);

            assertThat(result).usingRecursiveComparison().isEqualTo(transaction);

            verify(accountUseCase, times(2)).searchById(ID);
            verify(accountUseCase).saveAll(accountsCaptor.capture());
            verify(transactionGateway).save(any(Transaction.class));

            var transactionSaved = transactionCaptor.getValue();

            assertThat(transactionSaved)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt", "financialTransactionOrigin.id", "financialTransactionDestination.id")
                    .isEqualTo(transaction);
        }

        @Test
        void whenCreateTransferAndFromAccountHasZeroBalanceThenThrowTransactionDeclinedException() {
            var transaction = TransactionMock.createWithFromAccountBalance(ZERO);

            when(accountUseCase.searchById(ID))
                    .thenReturn(transaction.getFromAccount())
                    .thenReturn(transaction.getToAccount());

            assertThatThrownBy(() -> transactionUseCase.createTransfer(ID, ID, TEN))
                    .isInstanceOf(TransactionDeclinedException.class);

            verify(accountUseCase, times(2)).searchById(ID);
            verify(accountUseCase, never()).saveAll(anyList());
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateTransferAndFromAccountHasNegativeBalanceThenThrowTransactionDeclinedException() {
            var transaction = TransactionMock.createWithFromAccountBalance(new BigDecimal(-4));

            when(accountUseCase.searchById(ID))
                    .thenReturn(transaction.getFromAccount())
                    .thenReturn(transaction.getToAccount());

            assertThatThrownBy(() -> transactionUseCase.createTransfer(ID, ID, TEN))
                    .isInstanceOf(TransactionDeclinedException.class);

            verify(accountUseCase, times(2)).searchById(ID);
            verify(accountUseCase, never()).saveAll(anyList());
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateTransferAndFromAccountIsCanceledThenThrowException() {
            var transaction = TransactionMock.createWithAccountIsCanceled(CANCELED, ACTIVATED);

            when(accountUseCase.searchById(ID))
                    .thenReturn(transaction.getFromAccount())
                    .thenReturn(transaction.getToAccount());

            assertThatThrownBy(() -> transactionUseCase.createTransfer(ID, ID, TEN))
                    .isInstanceOf(AccountAlreadyCancelledException.class);

            verify(accountUseCase, times(2)).searchById(ID);
            verify(accountUseCase, never()).saveAll(anyList());
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateTransferAndToAccountIsCanceledThenThrowException() {
            var transaction = TransactionMock.createWithAccountIsCanceled(ACTIVATED, CANCELED);

            when(accountUseCase.searchById(ID))
                    .thenReturn(transaction.getFromAccount())
                    .thenReturn(transaction.getToAccount());

            assertThatThrownBy(() -> transactionUseCase.createTransfer(ID, ID, TEN))
                    .isInstanceOf(AccountAlreadyCancelledException.class);

            verify(accountUseCase, times(2)).searchById(ID);
            verify(accountUseCase, never()).saveAll(anyList());
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateTransferAndAccountNotFoundThenThrowBusinessException() {
            when(accountUseCase.searchById(ID)).thenThrow(new AccountNotFoundException());

            assertThatThrownBy(() -> transactionUseCase.createTransfer(ID, ID, TEN))
                    .isInstanceOf(BusinessException.class);

            verify(accountUseCase, times(1)).searchById(ID);
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

    }

    @Nested
    class CreateRefundTest {

        @Test
        void whenCreateRefundThenReturnTransaction() {
            var transaction = TransactionMock.createTransfer();
            var refundTransaction = TransactionMock.createRefund();

            when(transactionGateway.findById(ID)).thenReturn(Optional.of(transaction));
            when(transactionGateway.save(transactionCaptor.capture())).thenReturn(refundTransaction);

            var result = transactionUseCase.createRefund(ID);

            assertThat(result)
                    .isNotNull()
                    .usingRecursiveComparison().isEqualTo(refundTransaction);

            verify(transactionGateway).findById(ID);
            verify(accountUseCase).saveAll(anyList());
            verify(transactionGateway).save(any(Transaction.class));

            var refundSaved = transactionCaptor.getValue();

            assertThat(refundSaved)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt", "financialTransactionOrigin.id", "financialTransactionDestination.id")
                    .isEqualTo(refundTransaction);
        }

        @Test
        void whenCreateRefundAndFromAccountIsCanceledThenThrowException() {
            var transaction = TransactionMock.createWithAccountIsCanceled(CANCELED, ACTIVATED);

            when(transactionGateway.findById(ID)).thenReturn(Optional.of(transaction));

            assertThatThrownBy(() -> transactionUseCase.createRefund(ID))
                    .isInstanceOf(AccountAlreadyCancelledException.class);

            verify(transactionGateway).findById(ID);
            verify(accountUseCase, never()).saveAll(anyList());
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateRefundAndToAccountIsCanceledThenThrowException() {
            var transaction = TransactionMock.createWithAccountIsCanceled(ACTIVATED, CANCELED);

            when(transactionGateway.findById(ID)).thenReturn(Optional.of(transaction));

            assertThatThrownBy(() -> transactionUseCase.createRefund(ID))
                    .isInstanceOf(AccountAlreadyCancelledException.class);

            verify(transactionGateway).findById(ID);
            verify(accountUseCase, never()).saveAll(anyList());
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateRefundAndToAccountHasZeroBalanceThenThrowTransactionDeclinedException() {
            var transaction = TransactionMock.createWithToAccountBalance(ZERO);

            when(transactionGateway.findById(ID)).thenReturn(Optional.of(transaction));

            assertThatThrownBy(() -> transactionUseCase.createRefund(ID))
                    .isInstanceOf(TransactionDeclinedException.class);

            verify(transactionGateway).findById(ID);
            verify(accountUseCase, never()).saveAll(anyList());
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateRefundAndToAccountHasNegativeBalanceThenThrowTransactionDeclinedException() {
            var transaction = TransactionMock.createWithToAccountBalance(new BigDecimal(-4));

            when(transactionGateway.findById(ID)).thenReturn(Optional.of(transaction));

            assertThatThrownBy(() -> transactionUseCase.createRefund(ID))
                    .isInstanceOf(TransactionDeclinedException.class);

            verify(transactionGateway).findById(ID);
            verify(accountUseCase, never()).saveAll(anyList());
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateRefundAndTransactionNotFoundThenThrowTransactionNotFoundException() {
            when(transactionGateway.findById(ID)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> transactionUseCase.createRefund(ID))
                    .isInstanceOf(TransactionNotFoundException.class)
                    .hasMessageContaining(ID.toString());

            verify(transactionGateway).findById(ID);
            verify(accountUseCase, never()).saveAll(anyList());
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

    }

}