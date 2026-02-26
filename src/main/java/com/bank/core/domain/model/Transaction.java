package com.bank.core.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Transacción de cuenta bancaria.
 */
public final class Transaction {

    private final UUID id;
    private final TransactionType type;
    private final UUID sourceAccountId;
    private final UUID destinationAccountId;
    private final Money amount;
    private final LocalDateTime timestamp;

    public Transaction(
            UUID id,
            TransactionType type,
            UUID sourceAccountId,
            UUID destinationAccountId,
            Money amount,
            LocalDateTime timestamp
    ) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.type = Objects.requireNonNull(type, "type must not be null");
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = Objects.requireNonNull(amount, "amount must not be null");
        this.timestamp = Objects.requireNonNull(timestamp, "timestamp must not be null");
    }

    public UUID getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public UUID getSourceAccountId() {
        return sourceAccountId;
    }

    public UUID getDestinationAccountId() {
        return destinationAccountId;
    }

    public Money getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}

