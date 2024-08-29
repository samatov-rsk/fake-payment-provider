package com.samatov.payment_service.controller;

import com.samatov.payment_service.dto.PayoutRequestDto;
import com.samatov.payment_service.dto.PayoutResponseDto;
import com.samatov.payment_service.dto.TopUpRequestDto;
import com.samatov.payment_service.dto.TopUpResponseDto;
import com.samatov.payment_service.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "API для обработки платежей")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "Обработка запроса на пополнение счета",
            description = "Создает новую транзакцию на пополнение счета")
    @ApiResponse(responseCode = "200", description = "Успешное создание транзакции",
            content = @Content(schema = @Schema(implementation = TopUpResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    @PostMapping("/topups")
    public Mono<ResponseEntity<TopUpResponseDto>> processTopUp(@RequestBody TopUpRequestDto request) {
        return paymentService.processTopUp(request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Обработка запроса на вывод средств",
            description = "Создает новую транзакцию на вывод средств")
    @ApiResponse(responseCode = "200", description = "Успешное создание транзакции",
            content = @Content(schema = @Schema(implementation = PayoutResponseDto.class)))
    @ApiResponse(responseCode = "400", description = "Неверный запрос")
    @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    @PostMapping("/payout")
    public Mono<ResponseEntity<PayoutResponseDto>> processWithdrawal(@RequestBody PayoutRequestDto request) {
        return paymentService.processWithdrawal(request)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}