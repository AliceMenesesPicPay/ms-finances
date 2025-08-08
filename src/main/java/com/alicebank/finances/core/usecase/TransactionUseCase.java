package com.alicebank.finances.core.usecase;

import com.alicebank.finances.core.domain.FinancialTransaction;
import com.alicebank.finances.core.domain.Transaction;
import com.alicebank.finances.core.exception.AccountNotFoundException;
import com.alicebank.finances.core.exception.BusinessException;
import com.alicebank.finances.core.exception.TransactionNotFoundException;
import com.alicebank.finances.core.gateway.FinancialTransactionGateway;
import com.alicebank.finances.core.gateway.TransactionGateway;
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

    public Transaction searchById(final Long id) {
        return transactionGateway.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
    }

    @Transactional
    public Transaction createTransfer(final Transaction transaction) {
        try {
            transaction.validateAccounts();

            var fromAccount = accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transaction.getFromAccount());
            var toAccount = accountUseCase.searchByAccountCheckingAndNumberAndDigitAndAgency(transaction.getToAccount());

            transaction.transfer(fromAccount, toAccount);

            accountUseCase.saveAll(List.of(fromAccount, toAccount));
            var transactionSaved = transactionGateway.save(transaction);

            var financialTransactionDebit = FinancialTransaction.createTransferDebit(transactionSaved);
            var financialTransactionCredit = FinancialTransaction.createTransferCredit(transactionSaved);

            financialTransactionGateway.saveAll(List.of(financialTransactionDebit, financialTransactionCredit));
            return transactionSaved;
        } catch (AccountNotFoundException e) {
            throw new BusinessException(e);
        }
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

}
