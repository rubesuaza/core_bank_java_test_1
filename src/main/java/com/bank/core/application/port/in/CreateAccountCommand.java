package com.bank.core.application.port.in;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Comando de caso de uso para crear una nueva cuenta.
 */
public final class CreateAccountCommand {

    private final UUID ownerId;
    private final BigDecimal initialBalance;
    private final String currency;

    public CreateAccountCommand(UUID ownerId, BigDecimal initialBalance, String currency) {
        this.ownerId = Objects.requireNonNull(ownerId, "ownerId must not be null");
        this.initialBalance = Objects.requireNonNull(initialBalance, "initialBalance must not be null");
        this.currency = Objects.requireNonNull(currency, "currency must not be null");
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public BigDecimal getInitialBalance() {
        return initialBalance;
    }

    public String getCurrency() {
        return currency;
    }
}

