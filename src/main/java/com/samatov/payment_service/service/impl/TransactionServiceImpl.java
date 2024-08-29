package com.samatov.payment_service.service.impl;

import com.samatov.payment_service.dto.TransactionDto;
import com.samatov.payment_service.enums.TransactionStatus;
import com.samatov.payment_service.exception.TransactionNotFoundException;
import com.samatov.payment_service.mapper.TransactionMapper;
import com.samatov.payment_service.repository.TransactionRepository;
import com.samatov.payment_service.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Override
    public Mono<TransactionDto> findById(Long id) {
        log.debug("Finding transaction by id: {}", id);
        return transactionRepository.findById(id)
                .map(transactionMapper::toDto)
                .doOnSuccess(transaction -> log.debug("Found transaction: {}", transaction))
                .switchIfEmpty(Mono.error(new TransactionNotFoundException("Transaction not found with id: " + id)));
    }

    @Override
    public Flux<TransactionDto> findAll() {
        log.debug("Finding all transactions");
        return transactionRepository.findAll()
                .map(transactionMapper::toDto)
                .doOnComplete(() -> log.debug("Found all transactions"));
    }

    @Override
    public Flux<TransactionDto> findByStatus(TransactionStatus status) {
        log.debug("Finding transactions by status: {}", status);
        return transactionRepository.findByStatus(status)
                .map(transactionMapper::toDto)
                .doOnComplete(() -> log.debug("Found transactions with status: {}", status));
    }

    @Override
    public Flux<TransactionDto> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end) {
        log.debug("Finding transactions between {} and {}", start, end);
        return transactionRepository.findByCreatedAtBetween(start, end)
                .map(transactionMapper::toDto)
                .doOnComplete(() -> log.debug("Found transactions between {} and {}", start, end));
    }

    @Override
    public Mono<TransactionDto> createTransaction(TransactionDto transactionDto) {
        log.debug("Creating new transaction: {}", transactionDto);
        return transactionRepository.save(transactionMapper.toEntity(transactionDto))
                .map(transactionMapper::toDto)
                .doOnSuccess(transaction -> log.info("Created new transaction: {}", transaction));
    }

    @Override
    public Mono<TransactionDto> updateStatus(Long id, TransactionStatus status, String message) {
        log.debug("Updating status for transaction {}: {}", id, status);
        return transactionRepository.findById(id)
                .switchIfEmpty(Mono.error(new TransactionNotFoundException("Transaction not found with id: " + id)))
                .flatMap(transaction -> {
                    transaction.setStatus(status);
                    transaction.setMessage(message);
                    return transactionRepository.save(transaction);
                })
                .map(transactionMapper::toDto)
                .doOnSuccess(transaction -> log.info("Updated status for transaction {}: {}", id, status));
    }
}