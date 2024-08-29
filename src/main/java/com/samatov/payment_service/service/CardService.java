package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.CardDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardService {
    Mono<CardDto> findById(Long id);
    Flux<CardDto> findAll();
    Mono<CardDto> createCard(CardDto cardDto);
    Mono<CardDto> updateCard(Long id, CardDto cardDto);
    Mono<Void> deleteCard(Long id);
    Mono<CardDto> validateCard(String cardNumber, String expDate, String cvv);
}