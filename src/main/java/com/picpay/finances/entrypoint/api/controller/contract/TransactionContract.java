package com.picpay.finances.entrypoint.api.controller.contract;

import com.picpay.finances.entrypoint.api.controller.payload.request.TransactionRequest;
import com.picpay.finances.entrypoint.api.controller.payload.response.TransactionResponse;

import java.util.List;

public interface TransactionContract {

    TransactionResponse searchById(Long id);
    List<TransactionResponse> searchByCustomerId(Long customerId);
    TransactionResponse createTransfer(TransactionRequest transactionRequest);
    TransactionResponse createRefund(Long id);

}
