package com.samatov.payment_service.service.impl;

import com.samatov.payment_service.dto.MerchantDto;
import com.samatov.payment_service.exception.MerchantNotFoundException;
import com.samatov.payment_service.mapper.MerchantMapper;
import com.samatov.payment_service.repository.MerchantRepository;
import com.samatov.payment_service.service.MerchantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {
    private final MerchantRepository merchantRepository;
    private final MerchantMapper merchantMapper;

    @Override
    public Mono<MerchantDto> findById(Long id) {
        log.debug("Finding merchant by id: {}", id);
        return merchantRepository.findById(id)
                .map(merchantMapper::toDto)
                .doOnSuccess(merchant -> log.debug("Found merchant: {}", merchant))
                .switchIfEmpty(Mono.error(new MerchantNotFoundException("Merchant not found with id: " + id)));
    }

    @Override
    public Mono<MerchantDto> findByUserId(Long userId) {
        log.debug("Finding merchant by user id: {}", userId);
        return merchantRepository.findByUserId(userId)
                .map(merchantMapper::toDto)
                .doOnSuccess(merchant -> log.debug("Found merchant: {}", merchant))
                .switchIfEmpty(Mono.error(new MerchantNotFoundException("Merchant not found for user id: " + userId)));
    }

    @Override
    public Flux<MerchantDto> findAll() {
        log.debug("Finding all merchants");
        return merchantRepository.findAll()
                .map(merchantMapper::toDto)
                .doOnComplete(() -> log.debug("Found all merchants"));
    }

    @Override
    public Mono<MerchantDto> createMerchant(MerchantDto merchantDto) {
        log.debug("Creating new merchant: {}", merchantDto);
        return merchantRepository.save(merchantMapper.toEntity(merchantDto))
                .map(merchantMapper::toDto)
                .doOnSuccess(merchant -> log.info("Created new merchant: {}", merchant));
    }

    @Override
    public Mono<MerchantDto> updateMerchant(Long id, MerchantDto merchantDto) {
        log.debug("Updating merchant with id: {}", id);
        return merchantRepository.findById(id)
                .flatMap(existingMerchant -> {
                    existingMerchant.setUserId(merchantDto.getUserId());
                    return merchantRepository.save(existingMerchant);
                })
                .map(merchantMapper::toDto)
                .doOnSuccess(merchant -> log.info("Updated merchant: {}", merchant))
                .switchIfEmpty(Mono.error(new MerchantNotFoundException("Merchant not found with id: " + id)));
    }

    @Override
    public Mono<Void> deleteMerchant(Long id) {
        log.debug("Deleting merchant with id: {}", id);
        return merchantRepository.deleteById(id)
                .doOnSuccess(v -> log.info("Deleted merchant with id: {}", id));
    }

    @Override
    public Mono<MerchantDto> findDefaultMerchant() {
        return merchantRepository.findAll()
                .next()
                .map(merchantMapper::toDto)
                .switchIfEmpty(Mono.error(new MerchantNotFoundException("No merchants found in the system")));
    }
}



