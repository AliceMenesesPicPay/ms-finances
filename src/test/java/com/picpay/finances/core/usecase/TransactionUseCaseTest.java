package com.picpay.finances.core.usecase;

import com.picpay.finances.core.domain.*;
import com.picpay.finances.core.exception.*;
import com.picpay.finances.core.gateway.FinancialTransactionGateway;
import com.picpay.finances.core.gateway.TransactionGateway;
import com.picpay.finances.util.TransactionMock;
import com.picpay.finances.util.TransactionRequestMock;
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
import static com.picpay.finances.util.HelpTest.*;
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

    @Mock
    private FinancialTransactionGateway financialTransactionGateway;

    @InjectMocks
    private TransactionUseCase transactionUseCase;

    @Captor
    private ArgumentCaptor<List<FinancialTransaction>> financialTransactionsCaptor;

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
    class CreateTransferTest {

        @Test
        void whenCreateTransferThenReturnTransaction() {
            var transactionData = TransactionRequestMock.create().toTransaction();
            var transaction = TransactionMock.createTransfer();

            when(accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transactionData.getFromAccount()))
                    .thenReturn(transaction.getFromAccount());

            when(accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transactionData.getToAccount()))
                    .thenReturn(transaction.getToAccount());

            when(transactionGateway.save(transactionCaptor.capture())).thenReturn(transaction);

            var result = transactionUseCase.createTransfer(transactionData);

            assertThat(result).usingRecursiveComparison().isEqualTo(transaction);

            verify(accountUseCase, times(2)).searchByAccountCheckingAndNumberAndDigitAndAgency(any(Account.class));
            verify(accountUseCase).saveAll(anyList());
            verify(transactionGateway).save(any(Transaction.class));
            verify(financialTransactionGateway).saveAll(financialTransactionsCaptor.capture());

            var transactionSaved = transactionCaptor.getValue();
            var financialTransactions = financialTransactionsCaptor.getValue();

            assertThat(transactionSaved)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt")
                    .isEqualTo(transaction);

            assertThat(transactionSaved.getFromAccount().getBalance())
                    .isNotNull()
                    .isEqualByComparingTo(TEN.subtract(transaction.getAmount()));

            assertThat(transactionSaved.getToAccount().getBalance())
                    .isNotNull()
                    .isEqualByComparingTo(TEN.add(transaction.getAmount()));

            assertThat(financialTransactions)
                    .isNotEmpty()
                    .hasSize(2);

            assertThat(financialTransactions.get(0))
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("transaction.id", "transaction.createdAt", "transaction.updatedAt")
                    .isEqualTo(FinancialTransaction.createTransferDebit(transactionSaved));

            assertThat(financialTransactions.get(1))
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("transaction.id", "transaction.createdAt", "transaction.updatedAt")
                    .isEqualTo(FinancialTransaction.createTransferCredit(transactionSaved));
        }

        @Test
        void whenCreateTransferAndFromAccountHasZeroBalanceThenThrowTransactionDeclinedException() {
            var transactionData = TransactionRequestMock.create().toTransaction();
            var transaction = TransactionMock.createWithFromAccountBalance(ZERO);

            when(accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transactionData.getFromAccount()))
                    .thenReturn(transaction.getFromAccount());

            when(accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transactionData.getToAccount()))
                    .thenReturn(transaction.getToAccount());

            assertThatThrownBy(() -> transactionUseCase.createTransfer(transactionData))
                    .isInstanceOf(TransactionDeclinedException.class);

            verify(accountUseCase, times(2)).searchByAccountCheckingAndNumberAndDigitAndAgency(any(Account.class));
            verify(accountUseCase, never()).saveAll(anyList());
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateTransferAndFromAccountHasNegativeBalanceThenThrowTransactionDeclinedException() {
            var transactionData = TransactionRequestMock.create().toTransaction();
            var transaction = TransactionMock.createWithFromAccountBalance(new BigDecimal(-4));

            when(accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transactionData.getFromAccount()))
                    .thenReturn(transaction.getFromAccount());

            when(accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transactionData.getToAccount()))
                    .thenReturn(transaction.getToAccount());

            assertThatThrownBy(() -> transactionUseCase.createTransfer(transactionData))
                    .isInstanceOf(TransactionDeclinedException.class);

            verify(accountUseCase, times(2)).searchByAccountCheckingAndNumberAndDigitAndAgency(any(Account.class));
            verify(accountUseCase, never()).saveAll(anyList());
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateTransferAndFromAccountIsCanceledThenThrowException() {
            var transactionData = TransactionRequestMock.create().toTransaction();
            var transaction = TransactionMock.createWithAccountIsCanceled(CANCELED, ACTIVATED);

            when(accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(any(Account.class)))
                    .thenReturn(transaction.getFromAccount())
                    .thenReturn(transaction.getToAccount());

            assertThatThrownBy(() -> transactionUseCase.createTransfer(transactionData))
                    .isInstanceOf(AccountAlreadyCancelledException.class);

            verify(accountUseCase, times(2)).searchByAccountCheckingAndNumberAndDigitAndAgency(any(Account.class));
            verify(accountUseCase, never()).saveAll(anyList());
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateTransferAndToAccountIsCanceledThenThrowException() {
            var transactionData = TransactionRequestMock.create().toTransaction();
            var transaction = TransactionMock.createWithAccountIsCanceled(ACTIVATED, CANCELED);

            when(accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(any(Account.class)))
                    .thenReturn(transaction.getFromAccount())
                    .thenReturn(transaction.getToAccount());

            assertThatThrownBy(() -> transactionUseCase.createTransfer(transactionData))
                    .isInstanceOf(AccountAlreadyCancelledException.class);

            verify(accountUseCase, times(2)).searchByAccountCheckingAndNumberAndDigitAndAgency(any(Account.class));
            verify(accountUseCase, never()).saveAll(anyList());
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateTransferAndAccountNotFoundThenThrowBusinessException() {
            var transactionData = TransactionRequestMock.create().toTransaction();

            when(accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(any(Account.class))).thenThrow(new AccountNotFoundException());

            assertThatThrownBy(() -> transactionUseCase.createTransfer(transactionData))
                    .isInstanceOf(BusinessException.class);

            verify(accountUseCase, times(1)).searchByAccountCheckingAndNumberAndDigitAndAgency(any(Account.class));
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateTransferAndAccountDigitNotValidThenThrowDigitIsNotValidException() {
            var transactionData = TransactionMock.createTransferData("2", AGENCY);

            assertThatThrownBy(() -> transactionUseCase.createTransfer(transactionData))
                    .isInstanceOf(DigitIsNotValidException.class);

            verify(accountUseCase, never()).searchByAccountCheckingAndNumberAndDigitAndAgency(any(Account.class));
            verify(transactionGateway, never()).save(any(Transaction.class));
        }

        @Test
        void whenCreateTransferAndAccountAgencyIsNotValidThenThrowAgencyIsNotValidException() {
            var transactionData = TransactionMock.createTransferData(DIGIT, "3");

            assertThatThrownBy(() -> transactionUseCase.createTransfer(transactionData))
                    .isInstanceOf(AgencyIsNotValidException.class);

            verify(accountUseCase, never()).searchByAccountCheckingAndNumberAndDigitAndAgency(any(Account.class));
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
            verify(financialTransactionGateway).saveAll(financialTransactionsCaptor.capture());

            var refundSaved = transactionCaptor.getValue();
            var financialTransactions = financialTransactionsCaptor.getValue();

            assertThat(refundSaved)
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("id", "createdAt", "updatedAt")
                    .isEqualTo(refundTransaction);

            assertThat(refundSaved.getFromAccount().getBalance())
                    .isNotNull()
                    .isEqualByComparingTo(TEN.add(transaction.getAmount()));

            assertThat(refundSaved.getToAccount().getBalance())
                    .isNotNull()
                    .isEqualByComparingTo(TEN.subtract(transaction.getAmount()));

            assertThat(financialTransactions)
                    .isNotEmpty()
                    .hasSize(2);

            assertThat(financialTransactions.get(0))
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("transaction.id", "transaction.createdAt", "transaction.updatedAt")
                    .isEqualTo(FinancialTransaction.createRefundDebit(refundSaved));

            assertThat(financialTransactions.get(1))
                    .isNotNull()
                    .usingRecursiveComparison()
                    .ignoringFields("transaction.id", "transaction.createdAt", "transaction.updatedAt")
                    .isEqualTo(FinancialTransaction.createRefundCredit(refundSaved));
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