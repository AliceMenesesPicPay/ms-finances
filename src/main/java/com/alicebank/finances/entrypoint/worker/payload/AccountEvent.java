package com.alicebank.finances.entrypoint.worker.payload;

import com.alicebank.finances.core.domain.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountEvent {

    private String number;
    private String digit;
    private String agency;

    public Account toAccount() {
        return Account.builder()
                .number(number)
                .digit(digit)
                .agency(agency)
                .build();
    }

}
