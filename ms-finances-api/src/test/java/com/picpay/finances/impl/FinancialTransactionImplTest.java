package com.picpay.finances.impl;

import com.picpay.finances.dataprovider.database.entity.FinancialTransactionEntity;
import com.picpay.finances.dataprovider.database.impl.FinancialTransactionImpl;
import com.picpay.finances.dataprovider.database.repository.FinancialTransactionRepository;
import com.picpay.finances.util.FinancialTransactionMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static com.picpay.finances.util.HelpTest.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FinancialTransactionImplTest {

    @Mock
    private FinancialTransactionRepository financialTransactionRepository;

    @InjectMocks
    private FinancialTransactionImpl financialTransactionImpl;

    @Captor
    private ArgumentCaptor<List<FinancialTransactionEntity>> financialTransactionEntityCaptor;

    @Test
    void whenFindByCustomerIdThenReturnFinancialTransactions() {
        var pageable = PageRequest.of(0, 10);
        var entity = FinancialTransactionEntity.fromFinancialTransaction(FinancialTransactionMock.createWithAccount());

        when(financialTransactionRepository.findByCustomerId(ID, pageable)).thenReturn(List.of(entity));

        var result = financialTransactionImpl.findByCustomerId(ID, pageable);

        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(entity.toFinancialTransaction());

        verify(financialTransactionRepository).findByCustomerId(ID, pageable);
    }

    @Test
    void whenSaveAllThenRepositorySaveAll() {
        var financialTransaction = FinancialTransactionMock.createWithAccount();
        var entity = FinancialTransactionEntity.fromFinancialTransaction(financialTransaction);

        financialTransactionImpl.saveAll(List.of(financialTransaction));

        verify(financialTransactionRepository).saveAll(financialTransactionEntityCaptor.capture());

        assertThat(financialTransactionEntityCaptor.getValue())
                .isNotNull()
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(entity);
    }

    @Test
    void whenSaveThenRepositorySave() {
        var financialTransaction = FinancialTransactionMock.createWithAccount();

        financialTransactionImpl.save(financialTransaction);

        verify(financialTransactionRepository).save(any(FinancialTransactionEntity.class));
    }

}