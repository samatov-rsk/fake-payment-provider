package com.samatov.payment_service.service.impl;

import com.samatov.payment_service.dto.UserDto;
import com.samatov.payment_service.exception.UserNotFoundException;
import com.samatov.payment_service.mapper.UserMapper;
import com.samatov.payment_service.repository.UserRepository;
import com.samatov.payment_service.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public Mono<UserDto> findById(Long id) {
        log.debug("Finding user by id: {}", id);
        return userRepository.findById(id)
                .map(userMapper::toDto)
                .doOnSuccess(user -> log.debug("Found user: {}", user))
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with id: " + id)));
    }

    @Override
    public Flux<UserDto> findAll() {
        log.debug("Finding all users");
        return userRepository.findAll()
                .map(userMapper::toDto)
                .doOnComplete(() -> log.debug("Found all users"));
    }

    @Override
    public Mono<UserDto> createUser(UserDto userDto) {
        log.debug("Creating new user: {}", userDto);
        return userRepository.save(userMapper.toEntity(userDto))
                .map(userMapper::toDto)
                .doOnSuccess(user -> log.info("Created new user: {}", user));
    }

    @Override
    public Mono<UserDto> updateUser(Long id, UserDto userDto) {
        log.debug("Updating user with id: {}", id);
        return userRepository.findById(id)
                .flatMap(existingUser -> {
                    existingUser.setUserType(userDto.getUserType());
                    return userRepository.save(existingUser);
                })
                .map(userMapper::toDto)
                .doOnSuccess(user -> log.info("Updated user: {}", user))
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found with id: " + id)));
    }

    @Override
    public Mono<Void> deleteUser(Long id) {
        log.debug("Deleting user with id: {}", id);
        return userRepository.deleteById(id)
                .doOnSuccess(v -> log.info("Deleted user with id: {}", id));
    }
}
