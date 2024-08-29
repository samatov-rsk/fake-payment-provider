package com.samatov.payment_service.controller;

import com.samatov.payment_service.dto.WebhookDto;
import com.samatov.payment_service.enums.WebhookStatus;
import com.samatov.payment_service.mapper.WebhookMapper;
import com.samatov.payment_service.service.WebhookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/webhooks")
@RequiredArgsConstructor
@Tag(name = "Webhook", description = "API для работы с вебхуками")
public class WebhookController {

    private final WebhookService webhookService;

    @Operation(summary = "Отправка вебхука",
            description = "Отправляет вебхук для указанной транзакции")
    @ApiResponse(responseCode = "200", description = "Успешная отправка вебхука",
            content = @Content(schema = @Schema(implementation = WebhookDto.class)))
    @ApiResponse(responseCode = "404", description = "Транзакция не найдена")
    @PostMapping("/send/{transactionId}")
    public Mono<ResponseEntity<Void>> sendWebhook(
            @PathVariable Long transactionId,
            @RequestParam String status) {
        return webhookService.sendWebhook(transactionId, status)
                .then(Mono.just(ResponseEntity.ok().<Void>build()))
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().<Void>build()));
    }

    @PostMapping("/result")
    public Mono<ResponseEntity<WebhookDto>> saveWebhookResult(@RequestBody WebhookDto webhookDto) {
        return webhookService.saveWebhookResult(webhookDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}