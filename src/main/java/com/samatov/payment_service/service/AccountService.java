package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.AccountDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface AccountService {
    Mono<AccountDto> findById(Long id);
    Mono<AccountDto> findByUserId(Long userId);
    Flux<AccountDto> findAll();
    Mono<AccountDto> createAccount(AccountDto accountDto);
    Mono<AccountDto> updateAccount(Long id, AccountDto accountDto);
    Mono<Void> deleteAccount(Long id);
    Mono<AccountDto> freezeFunds(Long accountId, BigDecimal amount);
    Mono<AccountDto> unfreezeFunds(Long accountId, BigDecimal amount);
    Mono<AccountDto> transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount);
}