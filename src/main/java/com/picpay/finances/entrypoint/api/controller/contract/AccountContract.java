package com.picpay.finances.entrypoint.api.controller.contract;

import com.picpay.finances.entrypoint.api.controller.payload.request.CustomerIdRequest;
import com.picpay.finances.entrypoint.api.controller.payload.request.DepositRequest;
import com.picpay.finances.entrypoint.api.controller.payload.response.AccountResponse;

import java.util.List;

public interface AccountContract {

    AccountResponse searchById(Long id);
    List<AccountResponse> searchByCustomerId(Long customerId);
    List<AccountResponse> create(CustomerIdRequest accountRequest);
    List<AccountResponse> cancel(CustomerIdRequest accountRequest);
    void deposit(DepositRequest depositRequest);

}
