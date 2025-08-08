package com.picpay.finances.controller;

import com.picpay.finances.core.domain.Transaction;
import com.picpay.finances.core.usecase.TransactionUseCase;
import com.picpay.finances.entrypoint.api.controller.payload.response.TransactionResponse;
import com.picpay.finances.entrypoint.api.controller.TransactionController;
import com.picpay.finances.util.TransactionMock;
import com.picpay.finances.util.TransactionRequestMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.picpay.finances.util.HelpTest.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionUseCase transactionUseCase;

    @InjectMocks
    private TransactionController transactionController;

    @Captor
    private ArgumentCaptor<Transaction> transactionCaptor;

    @Test
    void whenSearchByIdThenReturnTransactions() {
        var transaction = TransactionMock.createTransfer();

        when(transactionUseCase.searchById(ID)).thenReturn(transaction);

        var response = transactionController.searchById(ID);

        assertThat(response)
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(TransactionResponse.from(transaction));

        verify(transactionUseCase).searchById(ID);
    }

    @Test
    void whenCreateTransferThenReturnTransfer() {
        var transactionRequest = TransactionRequestMock.create();
        var transaction = TransactionMock.createTransfer();

        when(transactionUseCase.createTransfer(transactionCaptor.capture()))
                .thenReturn(transaction);

        var response = transactionController.createTransfer(transactionRequest);

        assertThat(transactionCaptor.getValue())
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(transactionRequest.toTransaction());

        assertThat(response)
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(TransactionResponse.from(transaction));

        verify(transactionUseCase).createTransfer(any(Transaction.class));
    }

    @Test
    void whenCreateRefundThenReturnRefund() {
        var transaction = TransactionMock.createRefund();

        when(transactionUseCase.createRefund(ID)).thenReturn(transaction);

        var response = transactionController.createRefund(ID);

        assertThat(response)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(TransactionResponse.from(transaction));

        verify(transactionUseCase).createRefund(ID);
    }

}