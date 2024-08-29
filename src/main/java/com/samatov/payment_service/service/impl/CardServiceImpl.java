package com.samatov.payment_service.service.impl;

import com.samatov.payment_service.dto.CardDto;
import com.samatov.payment_service.exception.CardNotFoundException;
import com.samatov.payment_service.exception.InvalidCardException;
import com.samatov.payment_service.repository.CardRepository;
import com.samatov.payment_service.mapper.CardMapper;
import com.samatov.payment_service.service.CardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {
    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    @Override
    public Mono<CardDto> findById(Long id) {
        log.debug("Finding card by id: {}", id);
        return cardRepository.findById(id)
                .map(cardMapper::toDto)
                .doOnNext(card -> log.debug("Found card: {}", card))
                .switchIfEmpty(Mono.error(new CardNotFoundException("Card not found with id: " + id)));
    }

    @Override
    public Flux<CardDto> findAll() {
        log.debug("Finding all cards");
        return cardRepository.findAll()
                .map(cardMapper::toDto)
                .doOnComplete(() -> log.debug("Found all cards"));
    }

    @Override
    public Mono<CardDto> createCard(CardDto cardDto) {
        log.debug("Creating new card: {}", cardDto);
        return cardRepository.save(cardMapper.toEntity(cardDto))
                .map(cardMapper::toDto)
                .doOnSuccess(card -> log.info("Created new card: {}", card));
    }

    @Override
    public Mono<CardDto> updateCard(Long id, CardDto cardDto) {
        log.debug("Updating card with id: {}", id);
        return cardRepository.findById(id)
                .flatMap(existingCard -> {
                    existingCard.setCardNumber(cardDto.getCardNumber());
                    existingCard.setExpDate(cardDto.getExpDate());
                    existingCard.setCvv(cardDto.getCvv());
                    return cardRepository.save(existingCard);
                })
                .map(cardMapper::toDto)
                .doOnSuccess(card -> log.info("Updated card: {}", card))
                .switchIfEmpty(Mono.error(new CardNotFoundException("Card not found with id: " + id)));
    }

    @Override
    public Mono<Void> deleteCard(Long id) {
        log.debug("Deleting card with id: {}", id);
        return cardRepository.deleteById(id)
                .doOnSuccess(v -> log.info("Deleted card with id: {}", id));
    }

    @Override
    public Mono<CardDto> validateCard(String cardNumber, String expDate, String cvv) {
        log.debug("Validating card: {}", cardNumber);
        return cardRepository.findByCardNumber(cardNumber)
                .switchIfEmpty(Mono.error(new CardNotFoundException("Card not found: " + cardNumber)))
                .filter(card -> card.getExpDate().equals(expDate) && card.getCvv().equals(cvv))
                .map(cardMapper::toDto)
                .switchIfEmpty(Mono.error(new InvalidCardException("Invalid card details")))
                .doOnSuccess(card -> log.info("Card validated successfully: {}", cardNumber));
    }
}