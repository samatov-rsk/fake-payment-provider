package com.samatov.payment_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Данные клиента")
public class CustomerDto {
    @Schema(description = "Уникальный идентификатор клиента", example = "1")
    private Long id;
    @Schema(description = "Идентификатор пользователя", example = "1")
    private Long userId;
    @Schema(description = "Имя клиента", example = "John")
    private String firstName;
    @Schema(description = "Фамилия клиента", example = "Doe")
    private String lastName;
    @Schema(description = "Страна клиента", example = "USA")
    private String country;

    @Schema(description = "Дата создания", example = "2023-05-01T10:40:00Z")
    private LocalDateTime createdAt;

    @Schema(description = "Дата обновления", example = "2023-05-01T10:40:00Z")
    private LocalDateTime updatedAt;
}