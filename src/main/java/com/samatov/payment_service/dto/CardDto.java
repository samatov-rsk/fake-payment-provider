package com.samatov.payment_service.dto;

import com.samatov.payment_service.enums.CardType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Данные карты")
public class CardDto {

    @Schema(description = "Уникальный идентификатор карты", example = "1")
    private Long id;

    @Schema(description = "Идентификатор аккаунта", example = "1")
    private Long accountId;

    @Schema(description = "Номер карты", example = "4111111111111111")
    private String cardNumber;

    @Schema(description = "Тип карты", example = "VISA")
    private CardType cardType;

    @Schema(description = "Дата истечения срока действия", example = "12/25")
    private String expDate;

    @Schema(description = "CVV код", example = "123")
    private String cvv;

    @Schema(description = "Дата создания", example = "2023-05-01T10:40:00Z")
    private LocalDateTime createdAt;

    @Schema(description = "Дата обновления", example = "2023-05-01T10:40:00Z")
    private LocalDateTime updatedAt;
}