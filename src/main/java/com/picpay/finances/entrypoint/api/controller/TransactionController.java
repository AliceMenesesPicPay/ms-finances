package com.picpay.finances.entrypoint.api.controller;

import com.picpay.finances.core.usecase.TransactionUseCase;
import com.picpay.finances.entrypoint.api.controller.contract.TransactionContract;
import com.picpay.finances.entrypoint.api.controller.payload.request.TransactionRequest;
import com.picpay.finances.entrypoint.api.controller.payload.response.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
public class TransactionController implements TransactionContract {

    private final TransactionUseCase transactionUseCase;

    @GetMapping("/{id}")
    @Override
    public TransactionResponse searchById(@PathVariable Long id) {
        var transaction = transactionUseCase.searchById(id);
        return TransactionResponse.from(transaction);
    }

    @GetMapping
    @Override
    public List<TransactionResponse> searchByCustomerId(@RequestParam Long customerId) {
        var transactions = transactionUseCase.searchByCustomerId(customerId);
        return transactions.stream()
                .map(TransactionResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(CREATED)
    public TransactionResponse createTransfer(@Valid @RequestBody TransactionRequest transactionRequest) {
        var transaction = transactionUseCase.createTransfer(transactionRequest.getFromAccountId(), transactionRequest.getToAccountId(), transactionRequest.getAmount());
        return TransactionResponse.from(transaction);
    }

    @PostMapping("/{id}/refund")
    @ResponseStatus(CREATED)
    public TransactionResponse createRefund(@PathVariable Long id) {
        var transaction = transactionUseCase.createRefund(id);
        return TransactionResponse.from(transaction);
    }

}
