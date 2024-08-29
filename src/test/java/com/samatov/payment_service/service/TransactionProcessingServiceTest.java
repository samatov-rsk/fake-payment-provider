package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.AccountDto;
import com.samatov.payment_service.dto.TransactionDto;
import com.samatov.payment_service.enums.TransactionStatus;
import com.samatov.payment_service.enums.TransactionType;
import com.samatov.payment_service.service.impl.TransactionProcessingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class TransactionProcessingServiceTest {

    @Mock
    private TransactionService transactionService;
    @Mock
    private AccountService accountService;
    @Mock
    private WebhookService webhookService;

    @InjectMocks
    private TransactionProcessingService transactionProcessingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testProcessTransactions() {
        TransactionDto transaction = new TransactionDto();
        transaction.setId(1L);
        transaction.setStatus(TransactionStatus.IN_PROGRESS);
        transaction.setType(TransactionType.TOP_UP);
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setAccountFrom(1L);
        transaction.setAccountTo(2L);

        TransactionDto updatedTransaction = new TransactionDto();
        updatedTransaction.setId(transaction.getId());
        updatedTransaction.setStatus(TransactionStatus.SUCCESS);
        updatedTransaction.setType(transaction.getType());
        updatedTransaction.setAmount(transaction.getAmount());
        updatedTransaction.setAccountFrom(transaction.getAccountFrom());
        updatedTransaction.setAccountTo(transaction.getAccountTo());

        when(transactionService.findByStatus(TransactionStatus.IN_PROGRESS))
                .thenReturn(Flux.just(transaction));
        when(transactionService.updateStatus(anyLong(), any(), any()))
                .thenReturn(Mono.just(updatedTransaction));
        when(accountService.transferFunds(any(), any(), any()))
                .thenReturn(Mono.just(new AccountDto()));
        when(accountService.unfreezeFunds(any(), any()))
                .thenReturn(Mono.just(new AccountDto()));
        when(webhookService.sendWebhook(anyLong(), any()))
                .thenReturn(Mono.empty());

        transactionProcessingService.processTransactions();

        verify(transactionService).findByStatus(TransactionStatus.IN_PROGRESS);
        verify(transactionService).updateStatus(anyLong(), any(), any());
        verify(accountService, times(1)).transferFunds(any(), any(), any());
        verify(webhookService).sendWebhook(anyLong(), any());
    }

    @Test
    void testProcessTransactionsWithFailedTransaction() {
        TransactionDto transaction = new TransactionDto();
        transaction.setId(1L);
        transaction.setStatus(TransactionStatus.IN_PROGRESS);
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setAccountFrom(1L);
        transaction.setAccountTo(2L);

        TransactionDto updatedTransaction = new TransactionDto();
        updatedTransaction.setId(transaction.getId());
        updatedTransaction.setStatus(TransactionStatus.FAILED);
        updatedTransaction.setType(transaction.getType());
        updatedTransaction.setAmount(transaction.getAmount());
        updatedTransaction.setAccountFrom(transaction.getAccountFrom());
        updatedTransaction.setAccountTo(transaction.getAccountTo());

        when(transactionService.findByStatus(TransactionStatus.IN_PROGRESS))
                .thenReturn(Flux.just(transaction));
        when(transactionService.updateStatus(anyLong(), any(), any()))
                .thenReturn(Mono.just(updatedTransaction));
        when(accountService.unfreezeFunds(any(), any()))
                .thenReturn(Mono.just(new AccountDto()));
        when(webhookService.sendWebhook(anyLong(), any()))
                .thenReturn(Mono.empty());

        transactionProcessingService.processTransactions();

        verify(transactionService).findByStatus(TransactionStatus.IN_PROGRESS);
        verify(transactionService).updateStatus(anyLong(), any(), any());
        verify(accountService, times(1)).unfreezeFunds(any(), any());
        verify(webhookService).sendWebhook(anyLong(), any());
    }
}
