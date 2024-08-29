package com.samatov.payment_service.service.impl;

import com.samatov.payment_service.dto.CustomerDto;
import com.samatov.payment_service.exception.CustomerNotFoundException;
import com.samatov.payment_service.repository.CustomerRepository;
import com.samatov.payment_service.mapper.CustomerMapper;
import com.samatov.payment_service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    public Mono<CustomerDto> findById(Long id) {
        log.debug("Finding customer by id: {}", id);
        return customerRepository.findById(id)
                .map(customerMapper::toDto)
                .doOnSuccess(customer -> log.debug("Found customer: {}", customer))
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with id: " + id)));
    }

    @Override
    public Mono<CustomerDto> findByUserId(Long userId) {
        log.debug("Finding customer by user id: {}", userId);
        return customerRepository.findByUserId(userId)
                .map(customerMapper::toDto)
                .doOnSuccess(customer -> log.debug("Found customer: {}", customer))
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found for user id: " + userId)));
    }

    @Override
    public Flux<CustomerDto> findAll() {
        log.debug("Finding all customers");
        return customerRepository.findAll()
                .map(customerMapper::toDto)
                .doOnComplete(() -> log.debug("Found all customers"));
    }

    @Override
    public Mono<CustomerDto> createCustomer(CustomerDto customerDto) {
        log.debug("Creating new customer: {}", customerDto);
        return customerRepository.save(customerMapper.toEntity(customerDto))
                .map(customerMapper::toDto)
                .doOnSuccess(customer -> log.info("Created new customer: {}", customer));
    }

    @Override
    public Mono<CustomerDto> updateCustomer(Long id, CustomerDto customerDto) {
        log.debug("Updating customer with id: {}", id);
        return customerRepository.findById(id)
                .flatMap(existingCustomer -> {
                    existingCustomer.setFirstName(customerDto.getFirstName());
                    existingCustomer.setLastName(customerDto.getLastName());
                    existingCustomer.setCountry(customerDto.getCountry());
                    return customerRepository.save(existingCustomer);
                })
                .map(customerMapper::toDto)
                .doOnSuccess(customer -> log.info("Updated customer: {}", customer))
                .switchIfEmpty(Mono.error(new CustomerNotFoundException("Customer not found with id: " + id)));
    }

    @Override
    public Mono<Void> deleteCustomer(Long id) {
        log.debug("Deleting customer with id: {}", id);
        return customerRepository.deleteById(id)
                .doOnSuccess(v -> log.info("Deleted customer with id: {}", id));
    }

    @Override
    public Mono<CustomerDto> findOrCreateCustomer(CustomerDto customerDto) {
        log.debug("Finding or creating customer: {}", customerDto);
        return customerRepository.findByFirstNameAndLastName(customerDto.getFirstName(), customerDto.getLastName())
                .map(customerMapper::toDto)
                .switchIfEmpty(Mono.defer(() -> {
                    log.info("Customer not found, creating new customer: {}", customerDto);
                    return customerRepository.save(customerMapper.toEntity(customerDto))
                            .map(customerMapper::toDto);
                }))
                .doOnSuccess(customer -> log.info("Found or created customer: {}", customer));
    }
}


