package ru.aston.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.aston.dto.NewUserDto;
import ru.aston.dto.UserDto;
import ru.aston.service.UserService;

@RestController
@RequestMapping(path = "/users/")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@RequestBody @Valid NewUserDto newUserDto) {
        return userService.postUser(newUserDto);
    }

    @GetMapping("/userId")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }
}
