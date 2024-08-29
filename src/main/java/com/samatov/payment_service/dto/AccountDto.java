package com.samatov.payment_service.dto;

import com.samatov.payment_service.enums.Currency;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "Данные аккаунта")
public class AccountDto {
    @Schema(description = "Уникальный идентификатор аккаунта", example = "1")
    private Long id;

    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long userId;

    @Schema(description = "Баланс аккаунта", example = "1000.00")
    private BigDecimal balance;

    @Schema(description = "Валюта аккаунта", example = "USD")
    private Currency currency;

    @Schema(description = "Замороженная сумма", example = "100.00")
    private BigDecimal frozenAmount;

    @Schema(description = "Дата создания", example = "2023-05-01T10:40:00Z")
    private LocalDateTime createdAt;

    @Schema(description = "Дата обновления", example = "2023-05-01T10:40:00Z")
    private LocalDateTime updatedAt;
}