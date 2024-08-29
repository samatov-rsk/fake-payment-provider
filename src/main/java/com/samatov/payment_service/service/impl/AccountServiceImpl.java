package com.samatov.payment_service.service.impl;

import com.samatov.payment_service.dto.AccountDto;
import com.samatov.payment_service.exception.AccountNotFoundException;
import com.samatov.payment_service.exception.InsufficientFundsException;
import com.samatov.payment_service.mapper.AccountMapper;
import com.samatov.payment_service.repository.AccountRepository;
import com.samatov.payment_service.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    public Mono<AccountDto> findById(Long id) {
        log.debug("Finding account by id: {}", id);
        return accountRepository.findById(id)
                .map(accountMapper::toDto)
                .doOnNext(account -> log.debug("Found account: {}", account))
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Account not found with id: " + id)));
    }

    @Override
    public Mono<AccountDto> findByUserId(Long userId) {
        log.debug("Finding account for user id: {}", userId);
        return accountRepository.findByUserId(userId)
                .map(accountMapper::toDto)
                .doOnNext(account -> log.debug("Found account: {}", account))
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Account not found for user id: " + userId)));
    }

    @Override
    public Flux<AccountDto> findAll() {
        log.debug("Finding all accounts");
        return accountRepository.findAll()
                .map(accountMapper::toDto)
                .doOnComplete(() -> log.debug("Found all accounts"));
    }

    @Override
    public Mono<AccountDto> createAccount(AccountDto accountDto) {
        log.debug("Creating new account: {}", accountDto);
        return accountRepository.save(accountMapper.toEntity(accountDto))
                .map(accountMapper::toDto)
                .doOnSuccess(account -> log.info("Created new account: {}", account));
    }

    @Override
    public Mono<AccountDto> updateAccount(Long id, AccountDto accountDto) {
        log.debug("Updating account with id: {}", id);
        return accountRepository.findById(id)
                .flatMap(existingAccount -> {
                    existingAccount.setBalance(accountDto.getBalance());
                    existingAccount.setCurrency(accountDto.getCurrency());
                    return accountRepository.save(existingAccount);
                })
                .map(accountMapper::toDto)
                .doOnSuccess(account -> log.info("Updated account: {}", account))
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Account not found with id: " + id)));
    }

    @Override
    public Mono<Void> deleteAccount(Long id) {
        log.debug("Deleting account with id: {}", id);
        return accountRepository.deleteById(id)
                .doOnSuccess(v -> log.info("Deleted account with id: {}", id));
    }

    @Override
    public Mono<AccountDto> freezeFunds(Long accountId, BigDecimal amount) {
        log.debug("Freezing funds for account {}: {}", accountId, amount);
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Account not found: " + accountId)))
                .flatMap(account -> {
                    if (account.getBalance().compareTo(amount) < 0) {
                        return Mono.error(new InsufficientFundsException("Insufficient funds for account: " + accountId));
                    }
                    account.setBalance(account.getBalance().subtract(amount));
                    account.setFrozenAmount(account.getFrozenAmount().add(amount));
                    return accountRepository.save(account);
                })
                .map(accountMapper::toDto)
                .doOnSuccess(account -> log.info("Funds frozen for account {}: {}", accountId, amount));
    }

    @Override
    public Mono<AccountDto> unfreezeFunds(Long accountId, BigDecimal amount) {
        log.debug("Unfreezing funds for account {}: {}", accountId, amount);
        return accountRepository.findById(accountId)
                .switchIfEmpty(Mono.error(new AccountNotFoundException("Account not found: " + accountId)))
                .flatMap(account -> {
                    if (account.getFrozenAmount().compareTo(amount) < 0) {
                        return Mono.error(new InsufficientFundsException("Insufficient frozen funds for account: " + accountId));
                    }
                    account.setBalance(account.getBalance().add(amount));
                    account.setFrozenAmount(account.getFrozenAmount().subtract(amount));
                    return accountRepository.save(account);
                })
                .map(accountMapper::toDto)
                .doOnSuccess(account -> log.info("Funds unfrozen for account {}: {}", accountId, amount));
    }

    @Override
    public Mono<AccountDto> transferFunds(Long fromAccountId, Long toAccountId, BigDecimal amount) {
        log.debug("Transferring funds from account {} to account {}: {}", fromAccountId, toAccountId, amount);
        return accountRepository.findById(fromAccountId)
                .switchIfEmpty(Mono.error(new AccountNotFoundException("From account not found: " + fromAccountId)))
                .flatMap(fromAccount -> {
                    if (fromAccount.getBalance().compareTo(amount) < 0) {
                        return Mono.error(new InsufficientFundsException("Insufficient funds for account: " + fromAccountId));
                    }
                    fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
                    return accountRepository.save(fromAccount)
                            .then(accountRepository.findById(toAccountId))
                            .switchIfEmpty(Mono.error(new AccountNotFoundException("To account not found: " + toAccountId)))
                            .flatMap(toAccount -> {
                                toAccount.setBalance(toAccount.getBalance().add(amount));
                                return accountRepository.save(toAccount);
                            });
                })
                .map(accountMapper::toDto)
                .doOnSuccess(account -> log.info("Funds transferred from account {} to account {}: {}", fromAccountId, toAccountId, amount));
    }
}