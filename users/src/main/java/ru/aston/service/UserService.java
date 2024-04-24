package ru.aston.service;

import ru.aston.dto.NewUserDto;
import ru.aston.dto.UserDto;

public interface UserService {

    UserDto postUser(NewUserDto newUserDto);

    UserDto getUserById(Long userId);
}
