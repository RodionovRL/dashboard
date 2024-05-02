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
public class NewAccountDto {
    @Positive(message = "UserId должен быть больше 0")
    @NotNull
    private Long userId;
    @PositiveOrZero(message = "На счёте не может быть отрицательной суммы")
    @Builder.Default
    private BigDecimal amount = BigDecimal.valueOf(0);
}
