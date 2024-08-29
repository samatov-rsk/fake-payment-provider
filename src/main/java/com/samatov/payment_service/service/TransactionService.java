package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.TransactionDto;
import com.samatov.payment_service.enums.TransactionStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface TransactionService {
    Mono<TransactionDto> findById(Long id);
    Flux<TransactionDto> findAll();
    Flux<TransactionDto> findByStatus(TransactionStatus status);
    Flux<TransactionDto> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    Mono<TransactionDto> createTransaction(TransactionDto transactionDto);
    Mono<TransactionDto> updateStatus(Long id, TransactionStatus status, String message);
}