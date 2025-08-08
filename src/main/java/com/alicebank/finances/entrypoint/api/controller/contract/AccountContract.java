package com.alicebank.finances.entrypoint.api.controller.contract;

import com.alicebank.finances.entrypoint.api.controller.payload.request.CustomerIdRequest;
import com.alicebank.finances.entrypoint.api.controller.payload.request.DepositRequest;
import com.alicebank.finances.entrypoint.api.controller.payload.response.AccountResponse;

import java.util.List;

public interface AccountContract {

    AccountResponse searchById(Long id);
    List<AccountResponse> searchByCustomerId(Long customerId);
    List<AccountResponse> create(CustomerIdRequest accountRequest);
    List<AccountResponse> cancel(CustomerIdRequest accountRequest);
    void deposit(DepositRequest depositRequest);

}
