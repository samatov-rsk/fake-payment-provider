package com.samatov.payment_service.repository;

import com.samatov.payment_service.model.Card;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface CardRepository extends ReactiveCrudRepository<Card, Long> {
    Mono<Card> findByCardNumber(String cardNumber);
}