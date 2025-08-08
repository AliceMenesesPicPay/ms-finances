package com.alicebank.finances.core.usecase;

import com.alicebank.finances.core.domain.Account;
import com.alicebank.finances.core.domain.FinancialTransaction;
import com.alicebank.finances.core.exception.AccountNotFoundException;
import com.alicebank.finances.core.gateway.AccountGateway;
import com.alicebank.finances.core.gateway.FinancialTransactionGateway;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.alicebank.finances.core.domain.Account.newChecking;
import static com.alicebank.finances.core.domain.Account.newSavings;

@Service
@RequiredArgsConstructor
public class AccountUseCase {

    private final AccountGateway accountGateway;
    private final FinancialTransactionGateway financialTransactionGateway;

    public Account searchById(final Long id) {
        return accountGateway.findById(id).orElseThrow(AccountNotFoundException::new);
    }

    public List<Account> searchByCustomerId(final Long customerId) {
        return accountGateway.findByCustomerId(customerId);
    }

    public Account searchByAccountCheckingAndNumberAndDigitAndAgency(final Account account) {
        return accountGateway.searchByAccountCheckingAndNumberAndDigitAndAgency(account)
                .orElseThrow(AccountNotFoundException::new);
    }

    public List<Account> create(final Long customerId) {
        var accountChecking = newChecking(customerId);
        var accountSavings = newSavings(customerId);

        return accountGateway.saveAll(List.of(accountChecking, accountSavings));
    }

    public List<Account> saveAll(final List<Account> accounts) {
        return accountGateway.saveAll(accounts);
    }

    public List<Account> cancel(final Long customerId) {
        var accounts = accountGateway.findByCustomerId(customerId);

        if (accounts.isEmpty()) {
            throw new AccountNotFoundException();
        }

        accounts.forEach(Account::cancel);

        return accountGateway.saveAll(accounts);
    }

    @Transactional
    public void deposit(final BigDecimal amount, final Account account) {
        var depositAccount = searchByAccountCheckingAndNumberAndDigitAndAgency(account);
        depositAccount.credit(amount);
        var financialTransaction = FinancialTransaction.createDeposit(amount, depositAccount);
        accountGateway.save(depositAccount);
        financialTransactionGateway.save(financialTransaction);
    }

}
