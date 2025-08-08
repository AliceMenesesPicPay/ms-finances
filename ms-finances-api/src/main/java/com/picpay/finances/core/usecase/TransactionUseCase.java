package com.picpay.finances.core.usecase;

import com.picpay.finances.core.domain.Account;
import com.picpay.finances.core.domain.FinancialTransaction;
import com.picpay.finances.core.domain.Transaction;
import com.picpay.finances.core.exception.AccountNotFoundException;
import com.picpay.finances.core.exception.BusinessException;
import com.picpay.finances.core.exception.TransactionNotFoundException;
import com.picpay.finances.core.gateway.CustomerGateway;
import com.picpay.finances.core.gateway.FinancialTransactionGateway;
import com.picpay.finances.core.gateway.TransactionGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionUseCase {

    private final AccountUseCase accountUseCase;
    private final TransactionGateway transactionGateway;
    private final FinancialTransactionGateway financialTransactionGateway;
    private final CustomerGateway customerGateway;

    public Transaction searchById(final Long id) {
        return transactionGateway.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Transactional
    public Transaction createTransfer(final Transaction transaction) {
        try {
            validateAndCreateTransfer(transaction);
            return saveTransfer(transaction);
        } catch (AccountNotFoundException e) {
            throw new BusinessException(e);
        }
    }

    public void validateAndCreateTransfer(final Transaction transaction) {
        transaction.validateAccounts();
        transaction.areSameAccounts();

        var fromAccount = accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transaction.getFromAccount());
        var toAccount = accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transaction.getToAccount());

        transaction.transfer(fromAccount, toAccount);
    }

    public Transaction saveTransfer(final Transaction transaction) {
        accountUseCase.saveAll(List.of(transaction.getFromAccount(), transaction.getToAccount()));
        var transactionSaved = transactionGateway.save(transaction);

        setCustomersName(transactionSaved.getFromAccount(), transactionSaved.getToAccount());

        var financialTransactionDebit = FinancialTransaction.createTransferDebit(transactionSaved);
        var financialTransactionCredit = FinancialTransaction.createTransferCredit(transactionSaved);

        financialTransactionGateway.saveAll(List.of(financialTransactionDebit, financialTransactionCredit));
        return transactionSaved;
    }

    private void setCustomersName(Account fromAccount, Account toAccount) {
        var customerNameFromAccount = customerGateway.getCustomerName(fromAccount.getCustomerId());
        fromAccount.addName(customerNameFromAccount);
        var customerNameToAccount = customerGateway.getCustomerName(toAccount.getCustomerId());
        toAccount.addName(customerNameToAccount);
    }

    @Transactional
    public Transaction createRefund(final Long id) {
        var transaction = searchById(id);

        transaction = transaction.refund();

        accountUseCase.saveAll(List.of(transaction.getFromAccount(), transaction.getToAccount()));

        var transactionSaved = transactionGateway.save(transaction);

        var financialTransactionDebit = FinancialTransaction.createRefundDebit(transactionSaved);
        var financialTransactionCredit = FinancialTransaction.createRefundCredit(transactionSaved);

        financialTransactionGateway.saveAll(List.of(financialTransactionDebit, financialTransactionCredit));
        return transactionSaved;
    }

    @Transactional
    public Transaction deposit(final Transaction transaction) {
        try {
            transaction.getToAccount().validate();

            var depositAccount = accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transaction.getToAccount());

            transaction.deposit(depositAccount);

            var transactionSaved = transactionGateway.save(transaction);

            var financialTransaction = FinancialTransaction.createDeposit(transactionSaved);
            accountUseCase.saveAll(List.of(depositAccount));
            financialTransactionGateway.save(financialTransaction);

            return transactionSaved;
        } catch (AccountNotFoundException e) {
            throw new BusinessException(e);
        }
    }

    public Transaction createScheduledTransfer(Transaction transaction) {
        transaction.validateAccounts();

        var fromAccount = accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transaction.getFromAccount());
        var toAccount = accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transaction.getToAccount());

        transaction.scheduledTransfer(fromAccount, toAccount);

        return transactionGateway.save(transaction);
    }

}
