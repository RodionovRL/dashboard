package com.aston.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class NewOrderDto {

    @NotBlank
    @Size(min = 10, max = 50)
    private String name;

    @NotBlank
    @Size(min = 10, max = 250)
    private String description;

}
