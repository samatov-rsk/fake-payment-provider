package com.samatov.payment_service.dto;

import com.samatov.payment_service.enums.TransactionStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Ответ на запрос вывода средств")
public class PayoutResponseDto {

    @Schema(description = "ID транзакции", example = "1234")
    private Long transactionId;

    @Schema(description = "Статус транзакции", example = "IN_PROGRESS")
    private TransactionStatus status;

    @Schema(description = "Сообщение", example = "Payout request processed successfully")
    private String message;
}