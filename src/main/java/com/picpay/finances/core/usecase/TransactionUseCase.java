package com.picpay.finances.core.usecase;

import com.picpay.finances.core.domain.Transaction;
import com.picpay.finances.core.exception.AccountNotFoundException;
import com.picpay.finances.core.exception.BusinessException;
import com.picpay.finances.core.exception.TransactionNotFoundException;
import com.picpay.finances.core.gateway.TransactionGateway;
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

    public Transaction searchById(final Long id) {
        return transactionGateway.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
    }

    public List<Transaction> searchByCustomerId(final Long customerId) {
        return transactionGateway.findByCustomerId(customerId);
    }

    @Transactional
    public Transaction createTransfer(final Long fromAccountId, final Long toAccountId, final BigDecimal amount) {
        try {
            var fromAccount = accountUseCase.searchById(fromAccountId);
            var toAccount = accountUseCase.searchById(toAccountId);

            var transaction = Transaction.newTransfer(fromAccount, toAccount, amount);

            accountUseCase.saveAll(List.of(fromAccount, toAccount));
            return transactionGateway.save(transaction);
        } catch (AccountNotFoundException e) {
            throw new BusinessException(e);
        }
    }

    @Transactional
    public Transaction createRefund(final Long id) {
        var transaction = searchById(id);

        transaction = transaction.newRefund();

        accountUseCase.saveAll(List.of(transaction.getFromAccount(), transaction.getToAccount()));

        return transactionGateway.save(transaction);
    }

}
