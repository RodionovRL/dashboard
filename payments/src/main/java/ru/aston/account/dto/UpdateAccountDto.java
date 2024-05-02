package ru.aston.account.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateAccountDto {
    @Positive(message = "UserId должен быть больше 0")
    @NotNull
    private Long userId;
    @PositiveOrZero(message = "На счёте не может быть отрицательной суммы")
    @NotNull
    private BigDecimal amount;
}
