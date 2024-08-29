package com.samatov.payment_service.service.impl;

import com.samatov.payment_service.dto.TransactionDto;
import com.samatov.payment_service.enums.TransactionStatus;
import com.samatov.payment_service.enums.TransactionType;
import com.samatov.payment_service.service.AccountService;
import com.samatov.payment_service.service.TransactionService;
import com.samatov.payment_service.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionProcessingService {

    private final TransactionService transactionService;
    private final AccountService accountService;
    private final WebhookService webhookService;
    private final Random random = new Random();

    @Scheduled(fixedRate = 6000)
    public void processTransactions() {
        log.info("Starting transaction processing");
        transactionService.findByStatus(TransactionStatus.IN_PROGRESS)
                .flatMap(this::processTransaction)
                .flatMap(transaction -> webhookService.sendWebhook(transaction.getId(), transaction.getStatus().toString()))
                .subscribe(
                        success -> log.info("Processed and sent webhook for transaction"),
                        error -> log.error("Error processing transaction", error),
                        () -> log.info("Finished processing transactions")
                );
    }

    private Mono<TransactionDto> processTransaction(TransactionDto transaction) {
        boolean isSuccess = random.nextDouble() < 0.8; // 80% success rate
        TransactionStatus newStatus = isSuccess ? TransactionStatus.SUCCESS : TransactionStatus.FAILED;
        String message = isSuccess ? "Transaction processed successfully" : "Transaction failed due to random chance";

        return transactionService.updateStatus(transaction.getId(), newStatus, message)
                .flatMap(updatedTransaction -> {
                    if (updatedTransaction.getType() == TransactionType.TOP_UP) {
                        return processTopUp(updatedTransaction);
                    } else {
                        return processWithdrawal(updatedTransaction);
                    }
                });
    }

    private Mono<TransactionDto> processTopUp(TransactionDto transaction) {
        if (transaction.getStatus() == TransactionStatus.SUCCESS) {
            return accountService.transferFunds(transaction.getAccountFrom(), transaction.getAccountTo(), transaction.getAmount())
                    .thenReturn(transaction);
        } else {
            return accountService.unfreezeFunds(transaction.getAccountFrom(), transaction.getAmount())
                    .thenReturn(transaction);
        }
    }

    private Mono<TransactionDto> processWithdrawal(TransactionDto transaction) {
        if (transaction.getStatus() == TransactionStatus.SUCCESS) {
            return accountService.transferFunds(transaction.getAccountFrom(), transaction.getAccountTo(), transaction.getAmount())
                    .thenReturn(transaction);
        } else {
            return accountService.unfreezeFunds(transaction.getAccountFrom(), transaction.getAmount())
                    .thenReturn(transaction);
        }
    }
}
