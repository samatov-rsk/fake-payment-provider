package com.samatov.payment_service.controller;

import com.samatov.payment_service.dto.AccountDto;
import com.samatov.payment_service.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account", description = "API для управления счетами")
public class AccountController {

    private final AccountService accountService;

    @Operation(summary = "Получить счет по ID",
            description = "Возвращает информацию о счете по его ID")
    @ApiResponse(responseCode = "200", description = "Счет успешно найден",
            content = @Content(schema = @Schema(implementation = AccountDto.class)))
    @ApiResponse(responseCode = "404", description = "Счет не найден")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<AccountDto>> getAccountById(@PathVariable Long id) {
        return accountService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить счет по ID пользователя",
            description = "Возвращает информацию о счете, связанном с указанным ID пользователя")
    @ApiResponse(responseCode = "200", description = "Счет успешно найден",
            content = @Content(schema = @Schema(implementation = AccountDto.class)))
    @ApiResponse(responseCode = "404", description = "Счет не найден")
    @GetMapping("/user/{userId}")
    public Mono<ResponseEntity<AccountDto>> getAccountByUserId(@PathVariable Long userId) {
        return accountService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить все счета",
            description = "Возвращает список всех счетов")
    @ApiResponse(responseCode = "200", description = "Список счетов успешно получен",
            content = @Content(schema = @Schema(implementation = AccountDto.class)))
    @GetMapping
    public Flux<AccountDto> getAllAccounts() {
        return accountService.findAll();
    }

    @Operation(summary = "Создать новый счет",
            description = "Создает новый счет с указанными данными")
    @ApiResponse(responseCode = "201", description = "Счет успешно создан",
            content = @Content(schema = @Schema(implementation = AccountDto.class)))
    @PostMapping
    public Mono<ResponseEntity<AccountDto>> createAccount(@RequestBody AccountDto accountDto) {
        return accountService.createAccount(accountDto)
                .map(createdAccount -> ResponseEntity.status(HttpStatus.CREATED).body(createdAccount));
    }

    @Operation(summary = "Обновить счет",
            description = "Обновляет информацию о счете с указанным ID")
    @ApiResponse(responseCode = "200", description = "Счет успешно обновлен",
            content = @Content(schema = @Schema(implementation = AccountDto.class)))
    @ApiResponse(responseCode = "404", description = "Счет не найден")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<AccountDto>> updateAccount(@PathVariable Long id, @RequestBody AccountDto accountDto) {
        return accountService.updateAccount(id, accountDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить счет",
            description = "Удаляет счет с указанным ID")
    @ApiResponse(responseCode = "204", description = "Счет успешно удален")
    @ApiResponse(responseCode = "404", description = "Счет не найден")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteAccount(@PathVariable Long id) {
        return accountService.deleteAccount(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Заморозить средства",
            description = "Замораживает указанную сумму на счете")
    @ApiResponse(responseCode = "200", description = "Средства успешно заморожены",
            content = @Content(schema = @Schema(implementation = AccountDto.class)))
    @ApiResponse(responseCode = "404", description = "Счет не найден")
    @PostMapping("/{id}/freeze")
    public Mono<ResponseEntity<AccountDto>> freezeFunds(@PathVariable Long id, @RequestParam BigDecimal amount) {
        return accountService.freezeFunds(id, amount)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Разморозить средства",
            description = "Размораживает указанную сумму на счете")
    @ApiResponse(responseCode = "200", description = "Средства успешно разморожены",
            content = @Content(schema = @Schema(implementation = AccountDto.class)))
    @ApiResponse(responseCode = "404", description = "Счет не найден")
    @PostMapping("/{id}/unfreeze")
    public Mono<ResponseEntity<AccountDto>> unfreezeFunds(@PathVariable Long id, @RequestParam BigDecimal amount) {
        return accountService.unfreezeFunds(id, amount)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Перевести средства",
            description = "Переводит указанную сумму с одного счета на другой")
    @ApiResponse(responseCode = "200", description = "Средства успешно переведены",
            content = @Content(schema = @Schema(implementation = AccountDto.class)))
    @ApiResponse(responseCode = "404", description = "Один из счетов не найден")
    @PostMapping("/transfer")
    public Mono<ResponseEntity<AccountDto>> transferFunds(
            @RequestParam Long fromAccountId,
            @RequestParam Long toAccountId,
            @RequestParam BigDecimal amount) {
        return accountService.transferFunds(fromAccountId, toAccountId, amount)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}