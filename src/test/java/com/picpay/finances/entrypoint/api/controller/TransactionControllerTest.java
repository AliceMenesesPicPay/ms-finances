package com.picpay.finances.entrypoint.api.controller;

import com.picpay.finances.core.usecase.TransactionUseCase;
import com.picpay.finances.entrypoint.api.controller.payload.response.TransactionResponse;
import com.picpay.finances.util.TransactionMock;
import com.picpay.finances.util.TransactionRequestMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.picpay.finances.util.HelpTest.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionUseCase transactionUseCase;

    @InjectMocks
    private TransactionController transactionController;


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
    void whenSearchByCustomerIdThenReturnTransactions() {
        var transaction1 = TransactionMock.createTransfer();
        var transaction2 = TransactionMock.createRefund();

        when(transactionUseCase.searchByCustomerId(ID)).thenReturn(List.of(transaction1, transaction2));

        var response = transactionController.searchByCustomerId(ID);

        assertThat(response)
                .isNotNull()
                .hasSize(2)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(
                        TransactionResponse.from(transaction1),
                        TransactionResponse.from(transaction2)
                );

        verify(transactionUseCase).searchByCustomerId(ID);
    }

    @Test
    void whenCreateTransferThenReturnTransfer() {
        var transactionRequest = TransactionRequestMock.create();
        var transaction = TransactionMock.createTransfer();

        when(transactionUseCase.createTransfer(transactionRequest.getFromAccountId(), transactionRequest.getToAccountId(), transactionRequest.getAmount()))
                .thenReturn(transaction);

        var response = transactionController.createTransfer(TransactionRequestMock.create());

        assertThat(response)
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(TransactionResponse.from(transaction));

        verify(transactionUseCase).createTransfer(transactionRequest.getFromAccountId(), transactionRequest.getToAccountId(), transactionRequest.getAmount());
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