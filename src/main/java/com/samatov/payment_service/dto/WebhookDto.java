package com.samatov.payment_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Schema(description = "Данные вебхука")
public class WebhookDto {

    @Schema(description = "ID вебхука", example = "1")
    private Long id;

    @Schema(description = "ID транзакции", example = "1234")
    private Long transactionId;

    @Schema(description = "Тело запроса", example = "{\"status\":\"SUCCESS\",\"transactionId\":1234}")
    private String requestBody;

    @Schema(description = "Тело ответа", example = "{\"received\":true}")
    private String responseBody;

    @Schema(description = "Статус вебхука", example = "SENT")
    private String status;

    @Schema(description = "Номер попытки", example = "1")
    private Integer attemptNumber;

    @Schema(description = "Дата создания", example = "2023-05-01T10:40:00Z")
    private LocalDateTime createdAt;

    @Schema(description = "Дата обновления", example = "2023-05-01T10:40:00Z")
    private LocalDateTime updatedAt;
}