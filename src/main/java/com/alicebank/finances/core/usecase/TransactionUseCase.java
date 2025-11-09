package com.alicebank.finances.core.usecase;

import com.alicebank.finances.core.domain.*;
import com.alicebank.finances.core.exception.AccountNotFoundException;
import com.alicebank.finances.core.exception.BusinessException;
import com.alicebank.finances.core.exception.TransactionNotFoundException;
import com.alicebank.finances.core.gateway.CustomerGateway;
import com.alicebank.finances.core.gateway.FinancialTransactionGateway;
import com.alicebank.finances.core.gateway.TransactionGateway;
import com.alicebank.finances.core.gateway.TransactionScheduledGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionUseCase {

    private final AccountUseCase accountUseCase;
    private final TransactionGateway transactionGateway;
    private final TransactionScheduledGateway transactionScheduledGateway;
    private final FinancialTransactionGateway financialTransactionGateway;
    private final CustomerGateway customerGateway;

    private static int COUNT = 1;

    public Transaction searchById(final Long id) {
        return transactionGateway.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Transactional
    public Transaction createTransfer(final Transaction transaction) {
        try {
            validate(transaction);
            transfer(transaction);
            return saveTransfer(transaction);
        } catch (AccountNotFoundException e) {
            throw new BusinessException(e);
        }
    }

    public void transfer(final Transaction transaction) {
        var fromAccount = accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transaction.getFromAccount());
        var toAccount = accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transaction.getToAccount());

        transaction.transfer(fromAccount, toAccount);
    }

    @Transactional
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

    public TransactionScheduled createScheduledTransfer(TransactionScheduled transactionScheduled) {
        validate(transactionScheduled);

        var fromAccount = accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transactionScheduled.getFromAccount());
        var toAccount = accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transactionScheduled.getToAccount());

        transactionScheduled.scheduledTransfer(fromAccount, toAccount);

        return transactionScheduledGateway.save(transactionScheduled);
    }

    public void validate(ValidateTransaction validateTransaction) {
        validateTransaction.validateAccounts();
        validateTransaction.areSameAccounts();
    }

    public void saveTransferScheduled(TransactionScheduled transactionScheduled) {
        if (transactionScheduled.getAmount().compareTo(new BigDecimal(1)) == 0) {
            COUNT = COUNT + 1;
            throw new BusinessException("Erro ao salvar transação agendada");
        }

        accountUseCase.saveAll(List.of(transactionScheduled.getFromAccount(), transactionScheduled.getToAccount()));
        var transactionSaved = transactionGateway.save(transactionScheduled.getTransaction());

        transactionScheduled.complete(transactionSaved);
        transactionScheduledGateway.save(transactionScheduled);

        var financialTransactionDebit = FinancialTransaction.createTransferDebit(transactionSaved);
        var financialTransactionCredit = FinancialTransaction.createTransferCredit(transactionSaved);

        financialTransactionGateway.saveAll(List.of(financialTransactionDebit, financialTransactionCredit));
    }

}
