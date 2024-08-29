package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.WebhookDto;
import reactor.core.publisher.Mono;

public interface WebhookService {
    Mono<Void> sendWebhook(Long transactionId, String status);
    Mono<WebhookDto> saveWebhookResult(WebhookDto webhookDto);
}