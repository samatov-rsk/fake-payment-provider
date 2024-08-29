package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.TransactionDto;
import com.samatov.payment_service.enums.TransactionStatus;
import com.samatov.payment_service.exception.TransactionNotFoundException;
import com.samatov.payment_service.mapper.TransactionMapper;
import com.samatov.payment_service.model.Transaction;
import com.samatov.payment_service.repository.TransactionRepository;
import com.samatov.payment_service.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для TransactionServiceImpl")
class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Должен найти транзакцию по ID")
    void shouldFindTransactionById() {
        Long transactionId = 1L;
        Transaction transaction = new Transaction();
        TransactionDto transactionDto = new TransactionDto();

        when(transactionRepository.findById(transactionId)).thenReturn(Mono.just(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDto);

        StepVerifier.create(transactionService.findById(transactionId))
                .expectNext(transactionDto)
                .verifyComplete();

        verify(transactionRepository).findById(transactionId);
        verify(transactionMapper).toDto(transaction);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если транзакция не найдена")
    void shouldThrowExceptionWhenTransactionNotFound() {
        Long transactionId = 1L;

        when(transactionRepository.findById(transactionId)).thenReturn(Mono.empty());

        StepVerifier.create(transactionService.findById(transactionId))
                .expectError(TransactionNotFoundException.class)
                .verify();

        verify(transactionRepository).findById(transactionId);
    }

    @Test
    @DisplayName("Должен найти все транзакции")
    void shouldFindAllTransactions() {
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        TransactionDto transactionDto1 = new TransactionDto();
        TransactionDto transactionDto2 = new TransactionDto();

        when(transactionRepository.findAll()).thenReturn(Flux.just(transaction1, transaction2));
        when(transactionMapper.toDto(transaction1)).thenReturn(transactionDto1);
        when(transactionMapper.toDto(transaction2)).thenReturn(transactionDto2);

        StepVerifier.create(transactionService.findAll())
                .expectNext(transactionDto1, transactionDto2)
                .verifyComplete();

        verify(transactionRepository).findAll();
        verify(transactionMapper, times(2)).toDto(any(Transaction.class));
    }

    @Test
    @DisplayName("Должен создать новую транзакцию")
    void shouldCreateTransaction() {
        TransactionDto inputDto = new TransactionDto();
        Transaction transaction = new Transaction();
        TransactionDto outputDto = new TransactionDto();

        when(transactionMapper.toEntity(inputDto)).thenReturn(transaction);
        when(transactionRepository.save(transaction)).thenReturn(Mono.just(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(outputDto);

        StepVerifier.create(transactionService.createTransaction(inputDto))
                .expectNext(outputDto)
                .verifyComplete();

        verify(transactionMapper).toEntity(inputDto);
        verify(transactionRepository).save(transaction);
        verify(transactionMapper).toDto(transaction);
    }

    @Test
    @DisplayName("Должен обновить статус транзакции")
    void shouldUpdateTransactionStatus() {
        Long transactionId = 1L;
        TransactionStatus newStatus = TransactionStatus.SUCCESS;
        Transaction transaction = new Transaction();
        TransactionDto transactionDto = new TransactionDto();
        String message = "OK";

        when(transactionRepository.findById(transactionId)).thenReturn(Mono.just(transaction));
        when(transactionRepository.save(transaction)).thenReturn(Mono.just(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDto);

        StepVerifier.create(transactionService.updateStatus(transactionId, newStatus,message))
                .expectNext(transactionDto)
                .verifyComplete();

        verify(transactionRepository).findById(transactionId);
        verify(transactionRepository).save(transaction);
        verify(transactionMapper).toDto(transaction);
    }

    @Test
    @DisplayName("Должен найти транзакции по диапазону дат")
    void shouldFindTransactionsByDateRange() {
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        TransactionDto transactionDto1 = new TransactionDto();
        TransactionDto transactionDto2 = new TransactionDto();

        when(transactionRepository.findByCreatedAtBetween(startDate, endDate))
                .thenReturn(Flux.just(transaction1, transaction2));
        when(transactionMapper.toDto(transaction1)).thenReturn(transactionDto1);
        when(transactionMapper.toDto(transaction2)).thenReturn(transactionDto2);

        StepVerifier.create(transactionService.findByCreatedAtBetween(startDate, endDate))
                .expectNext(transactionDto1, transactionDto2)
                .verifyComplete();

        verify(transactionRepository).findByCreatedAtBetween(startDate, endDate);
        verify(transactionMapper, times(2)).toDto(any(Transaction.class));
    }
}
