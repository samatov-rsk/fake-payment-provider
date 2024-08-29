package com.samatov.payment_service.controller;

import com.samatov.payment_service.dto.CardDto;
import com.samatov.payment_service.service.CardService;
import com.samatov.payment_service.mapper.CardMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/cards")
@RequiredArgsConstructor
@Tag(name = "Card", description = "API для управления картами")
public class CardController {

    private final CardService cardService;

    @Operation(summary = "Получить карту по ID",
            description = "Возвращает информацию о карте по ее ID")
    @ApiResponse(responseCode = "200", description = "Карта успешно найдена",
            content = @Content(schema = @Schema(implementation = CardDto.class)))
    @ApiResponse(responseCode = "404", description = "Карта не найдена")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CardDto>> getCardById(@PathVariable Long id) {
        return cardService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить все карты",
            description = "Возвращает список всех карт")
    @ApiResponse(responseCode = "200", description = "Список карт успешно получен",
            content = @Content(schema = @Schema(implementation = CardDto.class)))
    @GetMapping
    public Flux<CardDto> getAllCards() {
        return cardService.findAll();
    }

    @Operation(summary = "Создать новую карту",
            description = "Создает новую карту с указанными данными")
    @ApiResponse(responseCode = "201", description = "Карта успешно создана",
            content = @Content(schema = @Schema(implementation = CardDto.class)))
    @PostMapping
    public Mono<ResponseEntity<CardDto>> createCard(@RequestBody CardDto cardDto) {
        return cardService.createCard(cardDto)
                .map(createdCard -> ResponseEntity.status(HttpStatus.CREATED).body(createdCard));
    }

    @Operation(summary = "Обновить карту",
            description = "Обновляет информацию о карте с указанным ID")
    @ApiResponse(responseCode = "200", description = "Карта успешно обновлена",
            content = @Content(schema = @Schema(implementation = CardDto.class)))
    @ApiResponse(responseCode = "404", description = "Карта не найдена")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CardDto>> updateCard(@PathVariable Long id, @RequestBody CardDto cardDto) {
        return cardService.updateCard(id, cardDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить карту",
            description = "Удаляет карту с указанным ID")
    @ApiResponse(responseCode = "204", description = "Карта успешно удалена")
    @ApiResponse(responseCode = "404", description = "Карта не найдена")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCard(@PathVariable Long id) {
        return cardService.deleteCard(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Валидировать карту",
            description = "Проверяет валидность карты по ее данным")
    @ApiResponse(responseCode = "200", description = "Карта валидна",
            content = @Content(schema = @Schema(implementation = CardDto.class)))
    @ApiResponse(responseCode = "400", description = "Неверные данные карты")
    @PostMapping("/validate")
    public Mono<ResponseEntity<CardDto>> validateCard(@RequestBody CardDto cardDto) {
        return cardService.validateCard(cardDto.getCardNumber(), cardDto.getExpDate(), cardDto.getCvv())
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }
}