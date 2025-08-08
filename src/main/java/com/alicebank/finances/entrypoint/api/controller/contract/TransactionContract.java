package com.alicebank.finances.entrypoint.api.controller.contract;

import com.alicebank.finances.entrypoint.api.controller.payload.request.TransactionRequest;
import com.alicebank.finances.entrypoint.api.controller.payload.response.TransactionResponse;

public interface TransactionContract {

    TransactionResponse searchById(Long id);
    TransactionResponse createTransfer(TransactionRequest transactionRequest);
    TransactionResponse createRefund(Long id);

}
