package ru.aston.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewOrderDto {

    private Long id;

    @NotBlank
    @Size(min = 10, max = 50)
    private String name;

    @NotBlank
    @Size(min = 10, max = 250)
    private String description;

}
