package com.alicebank.finances.core.domain;

import com.alicebank.finances.core.exception.*;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.alicebank.finances.core.domain.AccountType.CHECKING;
import static com.alicebank.finances.core.domain.AccountType.SAVINGS;
import static com.alicebank.finances.core.domain.Status.ACTIVATED;
import static com.alicebank.finances.core.domain.Status.CANCELED;
import static java.math.BigDecimal.ZERO;

@Getter
@Builder
public class Account {

    private static final String AGENCY = "0001";

    private Long id;
    private String number;
    private String digit;
    private String agency;
    private BigDecimal balance;
    private AccountType accountType;
    private Status status;
    private Long customerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static Account newChecking(final Long customerId) {
        var number = createNumber();

        return Account.builder()
                .number(number)
                .digit(calculateDigit(number))
                .agency(AGENCY)
                .balance(ZERO)
                .accountType(CHECKING)
                .status(ACTIVATED)
                .customerId(customerId)
                .build();
    }

    public static Account newSavings(final Long customerId) {
        var number = createNumber();

        return Account.builder()
                .number(number)
                .digit(calculateDigit(number))
                .agency("0001")
                .balance(ZERO)
                .accountType(SAVINGS)
                .status(ACTIVATED)
                .customerId(customerId)
                .build();
    }

    private static String createNumber() {
        var random = new Random();
        var number = random.nextInt(1000000);
        return String.format("%05d", number);
    }

    private static String calculateDigit(String number) {
        List<Integer> results = new ArrayList<>();

        for (int numberPosition = 0; numberPosition < number.length(); numberPosition++) {
            var accountNumberDigit = number.substring(numberPosition, numberPosition + 1);
            for (int multiplier = 7; multiplier >= 2; multiplier--) {
                var result = Integer.parseInt(accountNumberDigit) * multiplier;
                results.add(result);
            }
        }

        var sum = results.stream().mapToInt(Integer::intValue).sum();
        var mod = sum % 11;
        var digit = mod == 0 ? 0 : 10 - mod;
        return String.valueOf(digit);
    }

    public void validate() {
        isAgencyValid();
        isDigitValid();
    }

    //TODO colocar indice no banco para impedir criar conta com o msm numero
    private void isAgencyValid() {
        if (!agency.equals(AGENCY)) {
            throw new AgencyIsNotValidException(agency);
        }
    }

    private void isDigitValid() {
        if (!digit.equals(calculateDigit(number))) {
            throw new DigitIsNotValidException(number, digit);
        }
    }

    public void cancel() {
        if (status.isCanceled()) {
            throw new AccountAlreadyCancelledException(id);
        }

        if (balance.compareTo(ZERO) != 0) {
            throw new AccountHasBalanceException(id);
        }

        status = CANCELED;
    }

    public void debit(final BigDecimal amount) {
        var newBalance = balance.subtract(amount);

        if (newBalance.compareTo(ZERO) <= 0) {
            throw new TransactionDeclinedException(id);
        }

        if (status.isCanceled()) {
            throw new AccountAlreadyCancelledException(id);
        }

        balance = newBalance;
    }

    public void credit(final BigDecimal amount) {
        if (status.isCanceled()) {
            throw new AccountAlreadyCancelledException(id);
        }

        balance = balance.add(amount);
    }

}
