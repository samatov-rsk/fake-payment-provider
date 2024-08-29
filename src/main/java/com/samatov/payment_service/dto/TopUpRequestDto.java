package com.samatov.payment_service.dto;

import com.samatov.payment_service.enums.Currency;
import com.samatov.payment_service.enums.Language;
import com.samatov.payment_service.enums.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Запрос на пополнение счета")
public class TopUpRequestDto {

    @Schema(description = "Метод оплаты", example = "CARD")
    private PaymentMethod paymentMethod;

    @Schema(description = "Сумма пополнения", example = "100.00")
    private BigDecimal amount;

    @Schema(description = "Валюта", example = "USD")
    private Currency currency;

    @Schema(description = "Данные карты")
    private CardDataDto cardData;

    @Schema(description = "Язык", example = "EN")
    private Language language;

    @Schema(description = "URL для уведомлений", example = "https://example.com/webhook")
    private String notificationUrl;

    @Schema(description = "Данные клиента")
    private CustomerDto customer;
}