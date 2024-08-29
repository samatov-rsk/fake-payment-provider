package com.samatov.payment_service.service.impl;

import com.samatov.payment_service.dto.TransactionDto;
import com.samatov.payment_service.dto.WebhookDto;
import com.samatov.payment_service.mapper.WebhookMapper;
import com.samatov.payment_service.repository.WebhookRepository;
import com.samatov.payment_service.service.TransactionService;
import com.samatov.payment_service.service.WebhookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {
    private final WebhookRepository webhookRepository;
    private final WebhookMapper webhookMapper;
    private final TransactionService transactionService;
    private final WebClient webClient;

    @Override
    public Mono<Void> sendWebhook(Long transactionId, String status) {
        log.debug("Sending webhook for transaction {}: {}", transactionId, status);
        return transactionService.findById(transactionId)
                .flatMap(transaction -> {
                    String webhookUrl = transaction.getNotificationUrl();
                    String requestBody = createWebhookRequestBody(transaction, status);

                    return webClient.post()
                            .uri(webhookUrl)
                            .bodyValue(requestBody)
                            .retrieve()
                            .bodyToMono(String.class)
                            .flatMap(response -> saveWebhookResult(transaction.getId(), requestBody, response, "SUCCESS"))
                            .onErrorResume(e -> {
                                log.error("Error sending webhook for transaction {}", transactionId, e);
                                return saveWebhookResult(transaction.getId(), requestBody, e.getMessage(), "FAILED");
                            });
                })
                .then();
    }

    @Override
    public Mono<WebhookDto> saveWebhookResult(WebhookDto webhookDto) {
        return webhookRepository.save(webhookMapper.toEntity(webhookDto))
                .map(webhookMapper::toDto);
    }

    private Mono<WebhookDto> saveWebhookResult(Long transactionId, String requestBody, String responseBody, String status) {
        WebhookDto webhookDto = new WebhookDto();
        webhookDto.setTransactionId(transactionId);
        webhookDto.setRequestBody(requestBody);
        webhookDto.setResponseBody(responseBody);
        webhookDto.setStatus(status);
        webhookDto.setAttemptNumber(1);
        return saveWebhookResult(webhookDto);
    }

    private String createWebhookRequestBody(TransactionDto transaction, String status) {
        return String.format(
                "{\"transaction_id\":\"%s\",\"status\":\"%s\",\"amount\":\"%s\",\"currency\":\"%s\"}",
                transaction.getId(), status, transaction.getAmount(), transaction.getCurrency()
        );
    }
}
