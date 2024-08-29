package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.CustomerDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerDto> findById(Long id);
    Mono<CustomerDto> findByUserId(Long userId);
    Flux<CustomerDto> findAll();
    Mono<CustomerDto> createCustomer(CustomerDto customerDto);
    Mono<CustomerDto> updateCustomer(Long id, CustomerDto customerDto);
    Mono<Void> deleteCustomer(Long id);
    Mono<CustomerDto> findOrCreateCustomer(CustomerDto customerDto);
}