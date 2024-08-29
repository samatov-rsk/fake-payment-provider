package com.samatov.payment_service.Utils;

import com.samatov.payment_service.dto.UserDto;
import com.samatov.payment_service.model.User;
import com.samatov.payment_service.enums.UserType;

import java.time.LocalDateTime;

public class TestUtils {

    public static User createUser() {
        User user = new User();
        user.setId(1L);
        user.setUserType(UserType.CUSTOMER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return user;
    }

    public static UserDto createUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setUserType(UserType.CUSTOMER);
        userDto.setCreatedAt(LocalDateTime.now());
        userDto.setUpdatedAt(LocalDateTime.now());
        return userDto;
    }
}
