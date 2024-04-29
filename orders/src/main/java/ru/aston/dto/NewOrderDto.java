package ru.aston.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data transfer object (DTO) for creating a new order.
 * This DTO contains information about a new order, including its name and description.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewOrderDto {

    /**
     * The name of the order.
     * Must not be blank and must be between 10 and 50 characters long.
     */
    @NotBlank
    @Size(min = 10, max = 50)
    private String name;

    /**
     * The description of the order.
     * Must not be blank and must be between 10 and 250 characters long.
     */
    @NotBlank
    @Size(min = 10, max = 250)
    private String description;
}