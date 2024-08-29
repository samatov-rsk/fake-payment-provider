package com.samatov.payment_service.controller;

import com.samatov.payment_service.dto.CustomerDto;
import com.samatov.payment_service.service.CustomerService;
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
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(name = "Customer", description = "API для управления клиентами")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Получить клиента по ID",
            description = "Возвращает информацию о клиенте по его ID")
    @ApiResponse(responseCode = "200", description = "Клиент успешно найден",
            content = @Content(schema = @Schema(implementation = CustomerDto.class)))
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerDto>> getCustomerById(@PathVariable Long id) {
        return customerService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить клиента по ID пользователя",
            description = "Возвращает информацию о клиенте, связанном с указанным ID пользователя")
    @ApiResponse(responseCode = "200", description = "Клиент успешно найден",
            content = @Content(schema = @Schema(implementation = CustomerDto.class)))
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @GetMapping("/user/{userId}")
    public Mono<ResponseEntity<CustomerDto>> getCustomerByUserId(@PathVariable Long userId) {
        return customerService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Получить всех клиентов",
            description = "Возвращает список всех клиентов")
    @ApiResponse(responseCode = "200", description = "Список клиентов успешно получен",
            content = @Content(schema = @Schema(implementation = CustomerDto.class)))
    @GetMapping
    public Flux<CustomerDto> getAllCustomers() {
        return customerService.findAll();
    }

    @Operation(summary = "Создать нового клиента",
            description = "Создает нового клиента с указанными данными")
    @ApiResponse(responseCode = "201", description = "Клиент успешно создан",
            content = @Content(schema = @Schema(implementation = CustomerDto.class)))
    @PostMapping
    public Mono<ResponseEntity<CustomerDto>> createCustomer(@RequestBody CustomerDto customerDto) {
        return customerService.createCustomer(customerDto)
                .map(createdCustomer -> ResponseEntity.status(HttpStatus.CREATED).body(createdCustomer));
    }

    @Operation(summary = "Обновить клиента",
            description = "Обновляет информацию о клиенте с указанным ID")
    @ApiResponse(responseCode = "200", description = "Клиент успешно обновлен",
            content = @Content(schema = @Schema(implementation = CustomerDto.class)))
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerDto>> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto customerDto) {
        return customerService.updateCustomer(id, customerDto)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Удалить клиента",
            description = "Удаляет клиента с указанным ID")
    @ApiResponse(responseCode = "204", description = "Клиент успешно удален")
    @ApiResponse(responseCode = "404", description = "Клиент не найден")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable Long id) {
        return customerService.deleteCustomer(id)
                .then(Mono.just(ResponseEntity.noContent().<Void>build()))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Найти или создать клиента",
            description = "Находит существующего клиента или создает нового с указанными данными")
    @ApiResponse(responseCode = "200", description = "Клиент найден или успешно создан",
            content = @Content(schema = @Schema(implementation = CustomerDto.class)))
    @PostMapping("/find-or-create")
    public Mono<ResponseEntity<CustomerDto>> findOrCreateCustomer(@RequestBody CustomerDto customerDto) {
        return customerService.findOrCreateCustomer(customerDto)
                .map(ResponseEntity::ok);
    }
}