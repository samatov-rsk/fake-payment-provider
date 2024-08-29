package com.samatov.payment_service.repository;

import com.samatov.payment_service.model.Merchant;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface MerchantRepository extends ReactiveCrudRepository<Merchant, Long> {
    Mono<Merchant> findByUserId(Long userId);
}