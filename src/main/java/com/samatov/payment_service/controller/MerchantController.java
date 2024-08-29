package com.samatov.payment_service.controller;

import com.samatov.payment_service.dto.MerchantDto;
import com.samatov.payment_service.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/merchants")
@RequiredArgsConstructor
@Tag(name = "Merchant", description = "API для управления мерчантами")
public class MerchantController {

    private final MerchantService merchantService;

    @Operation(summary = "Получить мерчанта по ID",
            description = "Возвращает информацию о мерчанте по его ID")
    @ApiResponse(responseCode = "200", description = "Мерчант успешно найден",
            content = @Content(schema = @Schema(implementation = MerchantDto.class)))
    @ApiResponse(responseCode = "404", description = "Мерчант не найден")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<MerchantDto>> getMerchantById(@PathVariable Long id) {
        return merchantService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить мерчанта по ID пользователя",
            description = "Возвращает информацию о мерчанте, связанном с указанным ID пользователя")
    @ApiResponse(responseCode = "200", description = "Мерчант успешно найден",
            content = @Content(schema = @Schema(implementation = MerchantDto.class)))
    @ApiResponse(responseCode = "404", description = "Мерчант не найден")

    @GetMapping("/user/{userId}")
    public Mono<ResponseEntity<MerchantDto>> getMerchantByUserId(@PathVariable Long userId) {
        return merchantService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить всех мерчантов",
            description = "Возвращает список всех мерчантов")
    @ApiResponse(responseCode = "200", description = "Список мерчантов успешно получен",
            content = @Content(schema = @Schema(implementation = MerchantDto.class)))
    @GetMapping
    public Flux<MerchantDto> getAllMerchants() {
        return merchantService.findAll();
    }

    @Operation(summary = "Создать нового мерчанта",
            description = "Создает нового мерчанта с указанными данными")
    @ApiResponse(responseCode = "201", description = "Мерчант успешно создан",
            content = @Content(schema = @Schema(implementation = MerchantDto.class)))
    @PostMapping
    public Mono<ResponseEntity<MerchantDto>> createMerchant(@RequestBody MerchantDto merchantDto) {
        return merchantService.createMerchant(merchantDto)
                .map(createdMerchant -> ResponseEntity.status(HttpStatus.CREATED).body(createdMerchant));
    }

    @Operation(summary = "Обновить мерчанта",
            description = "Обновляет информацию о мерчанте с указанным ID")
    @ApiResponse(responseCode = "200", description = "Мерчант успешно обновлен",
            content = @Content(schema = @Schema(implementation = MerchantDto.class)))
    @ApiResponse(responseCode = "404", description = "Мерчант не найден")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<MerchantDto>> updateMerchant(@PathVariable Long id, @RequestBody MerchantDto merchantDto) {
        return merchantService.updateMerchant(id, merchantDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить мерчанта",
            description = "Удаляет мерчанта с указанным ID")
    @ApiResponse(responseCode = "204", description = "Мерчант успешно удален")
    @ApiResponse(responseCode = "404", description = "Мерчант не найден")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteMerchant(@PathVariable Long id) {
        return merchantService.deleteMerchant(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}