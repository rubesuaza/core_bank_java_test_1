package com.bank.core.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entidad que representa una transacción bancaria.
 */
public final class Transaction {

    private final UUID transactionId;
    private final UUID sourceAccountId;
    private final UUID destinationAccountId;
    private final Money amount;
    private final TransactionType type;
    private final LocalDateTime timestamp;

    public Transaction(
            UUID transactionId,
            UUID sourceAccountId,
            UUID destinationAccountId,
            Money amount,
            TransactionType type,
            LocalDateTime timestamp) {
        this.transactionId = transactionId;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    public UUID transactionId() {
        return transactionId;
    }

    public UUID sourceAccountId() {
        return sourceAccountId;
    }

    public UUID destinationAccountId() {
        return destinationAccountId;
    }

    public Money amount() {
        return amount;
    }

    public TransactionType type() {
        return type;
    }

    public LocalDateTime timestamp() {
        return timestamp;
    }
}
