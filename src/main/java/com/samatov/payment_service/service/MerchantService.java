package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.MerchantDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface MerchantService {
    Mono<MerchantDto> findById(Long id);
    Mono<MerchantDto> findByUserId(Long userId);
    Flux<MerchantDto> findAll();
    Mono<MerchantDto> createMerchant(MerchantDto merchantDto);
    Mono<MerchantDto> updateMerchant(Long id, MerchantDto merchantDto);
    Mono<Void> deleteMerchant(Long id);
    Mono<MerchantDto> findDefaultMerchant();
}