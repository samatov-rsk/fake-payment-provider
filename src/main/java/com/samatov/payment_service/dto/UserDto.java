package com.samatov.payment_service.dto;

import com.samatov.payment_service.enums.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "Данные пользователя")
public class UserDto {

    @Schema(description = "ID Пользователя", example = "1")
    private Long id;

    @Schema(description = "Тип пользователя", example = "MERCHANT")
    private UserType userType;

    @Schema(description = "Дата создания", example = "2023-05-01T10:40:00Z")
    private LocalDateTime createdAt;

    @Schema(description = "Дата обновления", example = "2023-05-01T10:40:00Z")
    private LocalDateTime updatedAt;
}