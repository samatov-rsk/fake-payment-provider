package com.samatov.payment_service.dto;

import com.samatov.payment_service.enums.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Данные транзакции")
public class TransactionDto {

    @Schema(description = "ID транзакции", example = "1234")
    private Long id;

    @Schema(description = "ID счета отправителя", example = "1")
    private Long accountFrom;

    @Schema(description = "ID счета получателя", example = "2")
    private Long accountTo;

    @Schema(description = "Сумма", example = "100.00")
    private BigDecimal amount;

    @Schema(description = "Валюта", example = "USD")
    private Currency currency;

    @Schema(description = "Метод оплаты", example = "CARD")
    private PaymentMethod paymentMethod;

    @Schema(description = "Номер карты (если применимо)", example = "4111111111111111")
    private String cardNumber;

    @Schema(description = "Язык", example = "EN")
    private Language language;

    @Schema(description = "URL для уведомлений", example = "https://example.com/webhook")
    private String notificationUrl;

    @Schema(description = "Статус транзакции", example = "SUCCESS")
    private TransactionStatus status;

    @Schema(description = "Сообщение", example = "Transaction processed successfully")
    private String message;

    @Schema(description = "Тип транзакции", example = "TOP_UP")
    private TransactionType type;

    @Schema(description = "Имя клиента", example = "John")
    private String customerFirstName;

    @Schema(description = "Фамилия клиента", example = "Doe")
    private String customerLastName;

    @Schema(description = "Страна клиента", example = "USA")
    private String customerCountry;

    @Schema(description = "Дата создания", example = "2023-05-01T10:30:00Z")
    private LocalDateTime createdAt;

    @Schema(description = "Дата обновления", example = "2023-05-01T10:35:00Z")
    private LocalDateTime updatedAt;
}