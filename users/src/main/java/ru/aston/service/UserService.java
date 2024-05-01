package ru.aston.service;

import ru.aston.dto.NewUserDto;
import ru.aston.dto.UserDto;

import java.util.List;

/**
 * Service interface for managing users.
 * This interface defines methods for creating and retrieving users.
 */
public interface UserService {

    /**
     * Creates a new user.
     * @param newUserDto The details of the new user.
     * @return The created user DTO.
     */
    UserDto postUser(NewUserDto newUserDto);

    /**
     * Retrieves a user by ID.
     * @param userId The ID of the user to retrieve.
     * @return The user DTO with the specified ID.
     */
    UserDto getUserById(Long userId);

    /**
     * Retrieves users by specified IDs.
     * @param users The list of IDs to retrieve users.
     * @return A list of user DTOs retrieved.
     */
    List<UserDto> getUserListByIds(List<Long> users);
}