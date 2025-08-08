package com.picpay.finances.entrypoint.api.controller;

import com.picpay.finances.core.usecase.AccountUseCase;
import com.picpay.finances.entrypoint.api.controller.contract.AccountContract;
import com.picpay.finances.entrypoint.api.controller.payload.request.CustomerIdRequest;
import com.picpay.finances.entrypoint.api.controller.payload.response.AccountResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
public class AccountController implements AccountContract {

    private final AccountUseCase accountUseCase;

    @GetMapping("/{id}")
    @Override
    public AccountResponse searchById(@PathVariable Long id) {
        var account = accountUseCase.searchById(id);
        return AccountResponse.from(account);
    }

    @GetMapping
    @Override
    public List<AccountResponse> searchByCustomerId(@RequestParam Long customerId) {
        var accounts = accountUseCase.searchByCustomerId(customerId);
        return accounts.stream()
                .map(AccountResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    @Override
    public List<AccountResponse> create(@Valid @RequestBody CustomerIdRequest customerIdRequest) {
        var accounts = accountUseCase.create(customerIdRequest.getCustomerId());
        return accounts.stream()
                .map(AccountResponse::from)
                .toList();
    }

    @PatchMapping("/cancel")
    @Override
    public List<AccountResponse> cancel(@Valid @RequestBody CustomerIdRequest customerIdRequest) {
        var accounts = accountUseCase.cancel(customerIdRequest.getCustomerId());
        return accounts.stream()
                .map(AccountResponse::from)
                .toList();
    }

}
