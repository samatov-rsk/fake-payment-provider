package com.samatov.payment_service.controller;

import com.samatov.payment_service.dto.TransactionDto;
import com.samatov.payment_service.enums.TransactionStatus;
import com.samatov.payment_service.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Transaction", description = "API для работы с транзакциями")
public class TransactionController {

    private final TransactionService transactionService;

    @Operation(summary = "Получение списка транзакций",
            description = "Возвращает список транзакций с возможностью фильтрации по дате")
    @ApiResponse(responseCode = "200", description = "Успешное получение списка транзакций",
            content = @Content(schema = @Schema(implementation = TransactionDto.class)))
    @GetMapping("/transaction/list")
    public Flux<TransactionDto> getTransactionList(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start_date,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end_date) {
        if (start_date != null && end_date != null) {
            return transactionService.findByCreatedAtBetween(start_date, end_date);
        }
        return transactionService.findAll();
    }

    @Operation(summary = "Получение деталей транзакции",
            description = "Возвращает детальную информацию о конкретной транзакции")
    @ApiResponse(responseCode = "200", description = "Успешное получение деталей транзакции",
            content = @Content(schema = @Schema(implementation = TransactionDto.class)))
    @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    @GetMapping("/transaction/{transactionId}/details")
    public Mono<ResponseEntity<TransactionDto>> getTransactionDetails(@PathVariable Long transactionId) {
        return transactionService.findById(transactionId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}