package ru.aston.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * Data transfer object (DTO) representing a user.
 * This DTO contains information about a user, including its ID and name.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Component
public class UserDto {

    /**
     * The ID of the user.
     */
    private Long id;

    /**
     * The name of the user.
     */
    private String name;
}