package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.AccountDto;
import com.samatov.payment_service.mapper.AccountMapper;
import com.samatov.payment_service.model.Account;
import com.samatov.payment_service.repository.AccountRepository;
import com.samatov.payment_service.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для AccountServiceImpl")
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountMapper accountMapper;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Должен найти счет по ID")
    void shouldFindAccountById() {
        Long accountId = 1L;
        Account account = new Account();
        AccountDto accountDto = new AccountDto();

        when(accountRepository.findById(accountId)).thenReturn(Mono.just(account));
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        StepVerifier.create(accountService.findById(accountId))
                .expectNext(accountDto)
                .verifyComplete();

        verify(accountRepository).findById(accountId);
        verify(accountMapper).toDto(account);
    }

    @Test
    @DisplayName("Должен найти счет по ID пользователя")
    void shouldFindAccountByUserId() {
        Long userId = 1L;
        Account account = new Account();
        AccountDto accountDto = new AccountDto();

        when(accountRepository.findByUserId(userId)).thenReturn(Mono.just(account));
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        StepVerifier.create(accountService.findByUserId(userId))
                .expectNext(accountDto)
                .verifyComplete();

        verify(accountRepository).findByUserId(userId);
        verify(accountMapper).toDto(account);
    }

    @Test
    @DisplayName("Должен найти все счета")
    void shouldFindAllAccounts() {
        Account account1 = new Account();
        Account account2 = new Account();
        AccountDto accountDto1 = new AccountDto();
        AccountDto accountDto2 = new AccountDto();

        when(accountRepository.findAll()).thenReturn(Flux.just(account1, account2));
        when(accountMapper.toDto(account1)).thenReturn(accountDto1);
        when(accountMapper.toDto(account2)).thenReturn(accountDto2);

        StepVerifier.create(accountService.findAll())
                .expectNext(accountDto1, accountDto2)
                .verifyComplete();

        verify(accountRepository).findAll();
        verify(accountMapper, times(2)).toDto(any(Account.class));
    }

    @Test
    @DisplayName("Должен создать новый счет")
    void shouldCreateAccount() {
        AccountDto inputDto = new AccountDto();
        Account account = new Account();
        AccountDto outputDto = new AccountDto();

        when(accountMapper.toEntity(inputDto)).thenReturn(account);
        when(accountRepository.save(account)).thenReturn(Mono.just(account));
        when(accountMapper.toDto(account)).thenReturn(outputDto);

        StepVerifier.create(accountService.createAccount(inputDto))
                .expectNext(outputDto)
                .verifyComplete();

        verify(accountMapper).toEntity(inputDto);
        verify(accountRepository).save(account);
        verify(accountMapper).toDto(account);
    }

    @Test
    @DisplayName("Должен обновить существующий счет")
    void shouldUpdateAccount() {
        Long accountId = 1L;
        AccountDto inputDto = new AccountDto();
        Account existingAccount = new Account();
        Account updatedAccount = new Account();
        AccountDto outputDto = new AccountDto();

        when(accountRepository.findById(accountId)).thenReturn(Mono.just(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(Mono.just(updatedAccount));
        when(accountMapper.toDto(updatedAccount)).thenReturn(outputDto);

        StepVerifier.create(accountService.updateAccount(accountId, inputDto))
                .expectNext(outputDto)
                .verifyComplete();

        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(existingAccount);
        verify(accountMapper).toDto(updatedAccount);
    }

    @Test
    @DisplayName("Должен удалить счет")
    void shouldDeleteAccount() {
        Long accountId = 1L;

        when(accountRepository.deleteById(accountId)).thenReturn(Mono.empty());

        StepVerifier.create(accountService.deleteAccount(accountId))
                .verifyComplete();

        verify(accountRepository).deleteById(accountId);
    }

    @Test
    @DisplayName("Должен заморозить средства на счете")
    void shouldFreezeFunds() {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("100.00");
        Account account = new Account();
        account.setBalance(new BigDecimal("200.00"));
        account.setFrozenAmount(BigDecimal.ZERO);
        AccountDto accountDto = new AccountDto();

        when(accountRepository.findById(accountId)).thenReturn(Mono.just(account));
        when(accountRepository.save(account)).thenReturn(Mono.just(account));
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        StepVerifier.create(accountService.freezeFunds(accountId, amount))
                .expectNext(accountDto)
                .verifyComplete();

        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(account);
        verify(accountMapper).toDto(account);
    }

    @Test
    @DisplayName("Должен разморозить средства на счете")
    void shouldUnfreezeFunds() {
        Long accountId = 1L;
        BigDecimal amount = new BigDecimal("100.00");
        Account account = new Account();
        account.setBalance(new BigDecimal("100.00"));
        account.setFrozenAmount(new BigDecimal("100.00"));
        AccountDto accountDto = new AccountDto();

        when(accountRepository.findById(accountId)).thenReturn(Mono.just(account));
        when(accountRepository.save(account)).thenReturn(Mono.just(account));
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        StepVerifier.create(accountService.unfreezeFunds(accountId, amount))
                .expectNext(accountDto)
                .verifyComplete();

        verify(accountRepository).findById(accountId);
        verify(accountRepository).save(account);
        verify(accountMapper).toDto(account);
    }

    @Test
    @DisplayName("Должен перевести средства между счетами")
    void shouldTransferFunds() {
        Long fromAccountId = 1L;
        Long toAccountId = 2L;
        BigDecimal amount = new BigDecimal("100.00");
        Account fromAccount = new Account();
        fromAccount.setBalance(new BigDecimal("200.00"));
        Account toAccount = new Account();
        toAccount.setBalance(new BigDecimal("50.00"));
        AccountDto accountDto = new AccountDto();

        when(accountRepository.findById(fromAccountId)).thenReturn(Mono.just(fromAccount));
        when(accountRepository.findById(toAccountId)).thenReturn(Mono.just(toAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(Mono.just(new Account()));
        when(accountMapper.toDto(any(Account.class))).thenReturn(accountDto);

        StepVerifier.create(accountService.transferFunds(fromAccountId, toAccountId, amount))
                .expectNext(accountDto)
                .verifyComplete();

        verify(accountRepository).findById(fromAccountId);
        verify(accountRepository).findById(toAccountId);
        verify(accountRepository, times(2)).save(any(Account.class));
        verify(accountMapper).toDto(any(Account.class));
    }
}
