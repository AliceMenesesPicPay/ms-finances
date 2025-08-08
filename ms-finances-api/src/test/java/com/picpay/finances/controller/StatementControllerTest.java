package com.picpay.finances.controller;

import com.picpay.finances.core.usecase.StatementUseCase;
import com.picpay.finances.entrypoint.api.controller.payload.response.StatementResponse;
import com.picpay.finances.entrypoint.api.controller.StatementController;
import com.picpay.finances.util.StatementMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import static com.picpay.finances.util.HelpTest.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatementControllerTest {

    @Mock
    private StatementUseCase statementUseCase;

    @InjectMocks
    private StatementController statementController;

    @Test
    void whenSearchByCustomerIdThenReturnStatementResponse() {
        var pageable = PageRequest.of(0, 10);
        var statement = StatementMock.create();

        when(statementUseCase.searchByCustomerId(ID, pageable)).thenReturn(statement);

        var response = statementController.searchByCustomerId(ID, pageable);

        assertThat(response)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(StatementResponse.from(statement));

        verify(statementUseCase).searchByCustomerId(ID, pageable);
    }

}