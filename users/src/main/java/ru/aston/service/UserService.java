package ru.aston.service;

import ru.aston.dto.NewUserDto;
import ru.aston.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto postUser(NewUserDto newUserDto);

    UserDto getUserById(Long userId);

    List<UserDto> getUserListByIds(List<Long> users);
}
