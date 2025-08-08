package com.picpay.finances.entrypoint.api.controller.contract;

import com.picpay.finances.entrypoint.api.controller.payload.request.DepositRequest;
import com.picpay.finances.entrypoint.api.controller.payload.request.ScheduledTransactionRequest;
import com.picpay.finances.entrypoint.api.controller.payload.request.TransactionRequest;
import com.picpay.finances.entrypoint.api.controller.payload.response.TransactionResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

public interface TransactionContract {

    TransactionResponse searchById(Long id);
    TransactionResponse createTransfer(TransactionRequest transactionRequest);
    TransactionResponse createRefund(Long id);
    TransactionResponse deposit(DepositRequest depositRequest);
    TransactionResponse createScheduledTransfer(ScheduledTransactionRequest scheduledTransactionRequest);

}
