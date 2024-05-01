package ru.aston.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object (DTO) representing a new user.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserDto {

    /**
     * The name of the new user.
     */
    private String name;
}