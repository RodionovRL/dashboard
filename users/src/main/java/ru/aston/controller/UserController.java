package ru.aston.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.aston.dto.NewUserDto;
import ru.aston.dto.UserDto;
import ru.aston.service.UserService;

/**
 * Controller class for managing private user-related HTTP requests.
 * This controller handles creating and retrieving users.
 */
@RestController
@RequestMapping(path = "/users/")
@AllArgsConstructor
public class UserController {

    /**
     * Logger for the {@link UserController} class.
     */
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    /**
     * The UserService instance associated with this UserController.
     */
    private UserService userService;

    /**
     * Handles HTTP POST requests to create a new user.
     *
     * @param newUserDto The details of the new user to be created.
     * @return The created user DTO.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(@RequestBody @Valid NewUserDto newUserDto) {
        log.debug("Retrieved request to create user: {}", newUserDto);
        return userService.postUser(newUserDto);
    }

    /**
     * Handles HTTP GET requests to retrieve a user by their ID.
     *
     * @param userId The ID of the user to retrieve.
     * @return The user DTO corresponding to the given ID.
     */
    @GetMapping("/userId")
    @ResponseStatus(HttpStatus.OK)
    public UserDto getUserById(@PathVariable Long userId) {
        log.debug("Retrieved request to get user by ID: {}", userId);
        return userService.getUserById(userId);
    }
}