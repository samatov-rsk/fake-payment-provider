package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDto> findById(Long id);
    Flux<UserDto> findAll();
    Mono<UserDto> createUser(UserDto userDto);
    Mono<UserDto> updateUser(Long id, UserDto userDto);
    Mono<Void> deleteUser(Long id);
}