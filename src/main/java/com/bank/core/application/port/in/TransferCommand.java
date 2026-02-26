package com.bank.core.application.port.in;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Comando de caso de uso para realizar una transferencia.
 */
public final class TransferCommand {

    private final UUID fromAccount;
    private final UUID toAccount;
    private final BigDecimal amount;

    public TransferCommand(UUID fromAccount, UUID toAccount, BigDecimal amount) {
        this.fromAccount = Objects.requireNonNull(fromAccount, "fromAccount must not be null");
        this.toAccount = Objects.requireNonNull(toAccount, "toAccount must not be null");
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
        if (this.amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
    }

    public UUID getFromAccount() {
        return fromAccount;
    }

    public UUID getToAccount() {
        return toAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TransferCommand that = (TransferCommand) o;
        return Objects.equals(fromAccount, that.fromAccount)
                && Objects.equals(toAccount, that.toAccount)
                && Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromAccount, toAccount, amount);
    }
}

