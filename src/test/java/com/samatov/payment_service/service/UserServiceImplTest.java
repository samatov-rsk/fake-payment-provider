package com.samatov.payment_service.service;

import com.samatov.payment_service.dto.UserDto;
import com.samatov.payment_service.exception.UserNotFoundException;
import com.samatov.payment_service.mapper.UserMapper;
import com.samatov.payment_service.model.User;
import com.samatov.payment_service.repository.UserRepository;
import com.samatov.payment_service.service.impl.UserServiceImpl;
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

@DisplayName("Тесты для UserServiceImpl")
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Должен найти пользователя по ID")
    void shouldFindUserById() {
        Long userId = 1L;
        User user = new User();
        UserDto userDto = new UserDto();

        when(userRepository.findById(userId)).thenReturn(Mono.just(user));
        when(userMapper.toDto(user)).thenReturn(userDto);

        StepVerifier.create(userService.findById(userId))
                .expectNext(userDto)
                .verifyComplete();

        verify(userRepository).findById(userId);
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("Должен выбросить исключение, если пользователь не найден")
    void shouldThrowExceptionWhenUserNotFound() {
        Long userId = 1L;

        when(userRepository.findById(userId)).thenReturn(Mono.empty());

        StepVerifier.create(userService.findById(userId))
                .expectError(UserNotFoundException.class)
                .verify();

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Должен найти всех пользователей")
    void shouldFindAllUsers() {
        User user1 = new User();
        User user2 = new User();
        UserDto userDto1 = new UserDto();
        UserDto userDto2 = new UserDto();

        when(userRepository.findAll()).thenReturn(Flux.just(user1, user2));
        when(userMapper.toDto(user1)).thenReturn(userDto1);
        when(userMapper.toDto(user2)).thenReturn(userDto2);

        StepVerifier.create(userService.findAll())
                .expectNext(userDto1, userDto2)
                .verifyComplete();

        verify(userRepository).findAll();
        verify(userMapper, times(2)).toDto(any(User.class));
    }

    @Test
    @DisplayName("Должен создать нового пользователя")
    void shouldCreateUser() {
        UserDto inputDto = new UserDto();
        User user = new User();
        UserDto outputDto = new UserDto();

        when(userMapper.toEntity(inputDto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(Mono.just(user));
        when(userMapper.toDto(user)).thenReturn(outputDto);

        StepVerifier.create(userService.createUser(inputDto))
                .expectNext(outputDto)
                .verifyComplete();

        verify(userMapper).toEntity(inputDto);
        verify(userRepository).save(user);
        verify(userMapper).toDto(user);
    }

    @Test
    @DisplayName("Должен обновить существующего пользователя")
    void shouldUpdateUser() {
        Long userId = 1L;
        UserDto inputDto = new UserDto();
        User existingUser = new User();
        User updatedUser = new User();
        UserDto outputDto = new UserDto();

        when(userRepository.findById(userId)).thenReturn(Mono.just(existingUser));
        when(userRepository.save(existingUser)).thenReturn(Mono.just(updatedUser));
        when(userMapper.toDto(updatedUser)).thenReturn(outputDto);

        StepVerifier.create(userService.updateUser(userId, inputDto))
                .expectNext(outputDto)
                .verifyComplete();

        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
        verify(userMapper).toDto(updatedUser);
    }

    @Test
    @DisplayName("Должен удалить пользователя")
    void shouldDeleteUser() {
        Long userId = 1L;

        when(userRepository.deleteById(userId)).thenReturn(Mono.empty());

        StepVerifier.create(userService.deleteUser(userId))
                .verifyComplete();

        verify(userRepository).deleteById(userId);
    }
}