package com.samatov.payment_service.repository;

import com.samatov.payment_service.enums.TransactionStatus;
import com.samatov.payment_service.model.Transaction;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface TransactionRepository extends ReactiveCrudRepository<Transaction, Long> {
    Flux<Transaction> findByStatus(TransactionStatus status);
    Flux<Transaction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
}