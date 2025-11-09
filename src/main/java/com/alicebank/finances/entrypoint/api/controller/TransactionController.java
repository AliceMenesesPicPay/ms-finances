package com.alicebank.finances.entrypoint.api.controller;

import com.alicebank.finances.core.usecase.TransactionUseCase;
import com.alicebank.finances.entrypoint.api.controller.contract.TransactionContract;
import com.alicebank.finances.entrypoint.api.controller.payload.request.DepositRequest;
import com.alicebank.finances.entrypoint.api.controller.payload.request.ScheduledTransactionRequest;
import com.alicebank.finances.entrypoint.api.controller.payload.request.TransactionRequest;
import com.alicebank.finances.entrypoint.api.controller.payload.response.TransactionResponse;
import com.alicebank.finances.entrypoint.api.controller.payload.response.TransactionScheduledResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @ResponseStatus(CREATED)
    public TransactionResponse createTransfer(@Valid @RequestBody TransactionRequest transactionRequest) {
        var transaction = transactionUseCase.createTransfer(transactionRequest.toTransaction());
        return TransactionResponse.from(transaction);
    }

    @PostMapping("/scheduled")
    @ResponseStatus(CREATED)
    public TransactionScheduledResponse createScheduledTransfer(@Valid @RequestBody ScheduledTransactionRequest scheduledTransactionRequest) {
        var transactionScheduled = transactionUseCase.createScheduledTransfer(scheduledTransactionRequest.toTransactionScheduled());
        return TransactionScheduledResponse.from(transactionScheduled);
    }

    @PostMapping("/{id}/refund")
    @ResponseStatus(CREATED)
    public TransactionResponse createRefund(@PathVariable Long id) {
        var transaction = transactionUseCase.createRefund(id);
        return TransactionResponse.from(transaction);
    }

    @PostMapping("/deposit")
    @ResponseStatus(CREATED)
    @Override
    public TransactionResponse deposit(@Valid @RequestBody DepositRequest depositRequest) {
        var transaction = transactionUseCase.deposit(depositRequest.toTransaction());
        return TransactionResponse.from(transaction);
    }

}
