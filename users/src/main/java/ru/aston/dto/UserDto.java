package ru.aston.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Data transfer object (DTO) representing a user.
 */
@Getter
@Setter
@AllArgsConstructor
public class UserDto {

    /**
     * The unique identifier of the user.
     */
    private Long id;

    /**
     * The name of the user.
     */
    private String name;
}