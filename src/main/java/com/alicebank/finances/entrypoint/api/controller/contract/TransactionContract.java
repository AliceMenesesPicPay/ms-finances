package com.alicebank.finances.entrypoint.api.controller.contract;

import com.alicebank.finances.entrypoint.api.controller.payload.request.DepositRequest;
import com.alicebank.finances.entrypoint.api.controller.payload.request.ScheduledTransactionRequest;
import com.alicebank.finances.entrypoint.api.controller.payload.request.TransactionRequest;
import com.alicebank.finances.entrypoint.api.controller.payload.response.TransactionResponse;
import com.alicebank.finances.entrypoint.api.controller.payload.response.TransactionScheduledResponse;

public interface TransactionContract {

    TransactionResponse searchById(Long id);
    TransactionResponse createTransfer(TransactionRequest transactionRequest);
    TransactionResponse createRefund(Long id);
    TransactionResponse deposit(DepositRequest depositRequest);
    TransactionScheduledResponse createScheduledTransfer(ScheduledTransactionRequest scheduledTransactionRequest);

}
