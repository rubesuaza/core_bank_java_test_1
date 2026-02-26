package com.bank.core.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class CreateAccountRequest {

    @NotNull
    private String ownerId;

    @NotNull
    @PositiveOrZero
    private BigDecimal initialBalance;

    @NotBlank
    private String currency;
}

