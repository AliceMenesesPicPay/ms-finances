package com.picpay.finances.dataprovider.database.impl;

import com.picpay.finances.dataprovider.database.entity.TransactionEntity;
import com.picpay.finances.dataprovider.database.repository.TransactionRepository;
import com.picpay.finances.util.TransactionMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.picpay.finances.util.HelpTest.ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransactionImpl transactionImpl;

    @Test
    void whenFindByIdThenReturnTransaction() {
        var transaction = TransactionMock.createTransfer();

        when(transactionRepository.findById(ID)).thenReturn(Optional.of(TransactionEntity.fromTransaction(transaction)));

        var result = transactionImpl.findById(ID);

        assertThat(result).isPresent();

        assertThat(result.get())
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(transaction);

        verify(transactionRepository).findById(ID);
    }

    @Test
    void whenFindByCustomerIdThenReturnTransactions() {
        var transaction = TransactionMock.createTransfer();

        when(transactionRepository.findByCustomerId(ID)).thenReturn(List.of(TransactionEntity.fromTransaction(transaction)));

        var result = transactionImpl.findByCustomerId(ID);

        assertThat(result)
                .isNotNull()
                .hasSize(1)
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactly(transaction);

        verify(transactionRepository).findByCustomerId(ID);
    }

    @Test
    void whenSaveThenReturnTransaction() {
        var transaction = TransactionMock.createTransfer();

        when(transactionRepository.save(any(TransactionEntity.class))).thenReturn(TransactionEntity.fromTransaction(transaction));

        var result = transactionImpl.save(transaction);

        assertThat(result)
                .isNotNull()
                .usingRecursiveComparison().isEqualTo(transaction);

        verify(transactionRepository).save(any(TransactionEntity.class));
    }

}