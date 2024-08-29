package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.CustomerDto;
import com.samatov.payment_service.exception.CustomerNotFoundException;
import com.samatov.payment_service.mapper.CustomerMapper;
import com.samatov.payment_service.model.Customer;
import com.samatov.payment_service.repository.CustomerRepository;
import com.samatov.payment_service.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("Тесты для CustomerServiceImpl")
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Должен найти клиента по ID")
    void shouldFindCustomerById() {
        Long customerId = 1L;
        Customer customer = new Customer();
        CustomerDto customerDto = new CustomerDto();

        when(customerRepository.findById(customerId)).thenReturn(Mono.just(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        StepVerifier.create(customerService.findById(customerId))
                .expectNext(customerDto)
                .verifyComplete();

        verify(customerRepository).findById(customerId);
        verify(customerMapper).toDto(customer);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если клиент не найден")
    void shouldThrowExceptionWhenCustomerNotFound() {
        Long customerId = 1L;

        when(customerRepository.findById(customerId)).thenReturn(Mono.empty());

        StepVerifier.create(customerService.findById(customerId))
                .expectError(CustomerNotFoundException.class)
                .verify();

        verify(customerRepository).findById(customerId);
    }

    @Test
    @DisplayName("Должен найти клиента по ID пользователя")
    void shouldFindCustomerByUserId() {
        Long userId = 1L;
        Customer customer = new Customer();
        CustomerDto customerDto = new CustomerDto();

        when(customerRepository.findByUserId(userId)).thenReturn(Mono.just(customer));
        when(customerMapper.toDto(customer)).thenReturn(customerDto);

        StepVerifier.create(customerService.findByUserId(userId))
                .expectNext(customerDto)
                .verifyComplete();

        verify(customerRepository).findByUserId(userId);
        verify(customerMapper).toDto(customer);
    }

    @Test
    @DisplayName("Должен найти всех клиентов")
    void shouldFindAllCustomers() {
        Customer customer1 = new Customer();
        Customer customer2 = new Customer();
        CustomerDto customerDto1 = new CustomerDto();
        CustomerDto customerDto2 = new CustomerDto();

        when(customerRepository.findAll()).thenReturn(Flux.just(customer1, customer2));
        when(customerMapper.toDto(customer1)).thenReturn(customerDto1);
        when(customerMapper.toDto(customer2)).thenReturn(customerDto2);

        StepVerifier.create(customerService.findAll())
                .expectNext(customerDto1, customerDto2)
                .verifyComplete();

        verify(customerRepository).findAll();
        verify(customerMapper, times(2)).toDto(any(Customer.class));
    }

    @Test
    @DisplayName("Должен создать нового клиента")
    void shouldCreateCustomer() {
        CustomerDto inputDto = new CustomerDto();
        Customer customer = new Customer();
        CustomerDto outputDto = new CustomerDto();

        when(customerMapper.toEntity(inputDto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(Mono.just(customer));
        when(customerMapper.toDto(customer)).thenReturn(outputDto);

        StepVerifier.create(customerService.createCustomer(inputDto))
                .expectNext(outputDto)
                .verifyComplete();

        verify(customerMapper).toEntity(inputDto);
        verify(customerRepository).save(customer);
        verify(customerMapper).toDto(customer);
    }

    @Test
    @DisplayName("Должен обновить существующего клиента")
    void shouldUpdateCustomer() {
        Long customerId = 1L;
        CustomerDto inputDto = new CustomerDto();
        Customer existingCustomer = new Customer();
        Customer updatedCustomer = new Customer();
        CustomerDto outputDto = new CustomerDto();

        when(customerRepository.findById(customerId)).thenReturn(Mono.just(existingCustomer));
        when(customerRepository.save(existingCustomer)).thenReturn(Mono.just(updatedCustomer));
        when(customerMapper.toDto(updatedCustomer)).thenReturn(outputDto);

        StepVerifier.create(customerService.updateCustomer(customerId, inputDto))
                .expectNext(outputDto)
                .verifyComplete();

        verify(customerRepository).findById(customerId);
        verify(customerRepository).save(existingCustomer);
        verify(customerMapper).toDto(updatedCustomer);
    }

    @Test
    @DisplayName("Должен удалить клиента")
    void shouldDeleteCustomer() {
        Long customerId = 1L;

        when(customerRepository.deleteById(customerId)).thenReturn(Mono.empty());

        StepVerifier.create(customerService.deleteCustomer(customerId))
                .verifyComplete();

        verify(customerRepository).deleteById(customerId);
    }

    @Test
    @DisplayName("Должен найти или создать клиента")
    void shouldFindOrCreateCustomer() {
        CustomerDto inputDto = new CustomerDto();
        Customer customer = new Customer();
        CustomerDto outputDto = new CustomerDto();

        when(customerRepository.findByFirstNameAndLastName(inputDto.getFirstName(), inputDto.getLastName()))
                .thenReturn(Mono.empty());
        when(customerMapper.toEntity(inputDto)).thenReturn(customer);
        when(customerRepository.save(customer)).thenReturn(Mono.just(customer));
        when(customerMapper.toDto(customer)).thenReturn(outputDto);

        StepVerifier.create(customerService.findOrCreateCustomer(inputDto))
                .expectNext(outputDto)
                .verifyComplete();

        verify(customerRepository).findByFirstNameAndLastName(inputDto.getFirstName(), inputDto.getLastName());
        verify(customerMapper).toEntity(inputDto);
        verify(customerRepository).save(customer);
        verify(customerMapper).toDto(customer);
    }
}
