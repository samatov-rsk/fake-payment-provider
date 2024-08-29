package com.samatov.payment_service.repository;

import com.samatov.payment_service.model.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Long> {
    Mono<Customer> findByUserId(Long userId);
    Mono<Customer> findByFirstNameAndLastName(String firstName, String lastName);
}