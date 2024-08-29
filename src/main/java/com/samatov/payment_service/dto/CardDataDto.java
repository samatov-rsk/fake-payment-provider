package com.samatov.payment_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Данные карты")
public class CardDataDto {

    @Schema(description = "Номер карты", example = "4111111111111111")
    private String cardNumber;

    @Schema(description = "Дата истечения срока действия", example = "12/25")
    private String expDate;

    @Schema(description = "CVV код", example = "123")
    private String cvv;
}