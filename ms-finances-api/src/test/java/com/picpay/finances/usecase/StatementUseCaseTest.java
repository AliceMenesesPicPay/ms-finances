package com.picpay.finances.usecase;

import com.picpay.finances.core.domain.FinancialTransaction;
import com.picpay.finances.core.domain.Statement;
import com.picpay.finances.core.gateway.FinancialTransactionGateway;
import com.picpay.finances.core.usecase.StatementUseCase;
import com.picpay.finances.util.FinancialTransactionMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.picpay.finances.util.HelpTest.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatementUseCaseTest {

    @Mock
    private FinancialTransactionGateway financialTransactionGateway;

    @InjectMocks
    private StatementUseCase statementUseCase;

    @Test
    void whenSearchByCustomerIdThenReturnStatement() {
        var pageable = PageRequest.of(0, 10);
        var transaction = FinancialTransactionMock.create();
        List<FinancialTransaction> transactions = List.of(transaction);

        when(financialTransactionGateway.findByCustomerId(ID, pageable)).thenReturn(transactions);

        var result = statementUseCase.searchByCustomerId(ID, pageable);

        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFields("createdAt")
                .isEqualTo(Statement.create(transactions));

        assertThat(result.getCreatedAt())
                .isNotNull();
    }


}