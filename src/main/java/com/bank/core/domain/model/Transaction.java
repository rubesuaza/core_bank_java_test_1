package com.bank.core.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidad que representa una transacción bancaria.
 */
public class Transaction {

    private final UUID transactionId;
    private final UUID sourceAccountId;
    private final UUID destinationAccountId;
    private final Money amount;
    private final TransactionType type;
    private final LocalDateTime timestamp;

    public Transaction(UUID transactionId, UUID sourceAccountId, UUID destinationAccountId,
                       Money amount, TransactionType type, LocalDateTime timestamp) {
        if (transactionId == null || sourceAccountId == null || amount == null ||
                type == null || timestamp == null) {
            throw new IllegalArgumentException("transactionId, sourceAccountId, amount, type and timestamp must be non-null");
        }
        if (amount.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new com.bank.core.domain.exception.InvalidAmountException(
                    "Transaction amount must be strictly greater than zero: " + amount.getAmount());
        }
        if (type == TransactionType.TRANSFER && destinationAccountId == null) {
            throw new IllegalArgumentException("destinationAccountId is required for TRANSFER transactions");
        }
        if (type == TransactionType.TRANSFER && sourceAccountId.equals(destinationAccountId)) {
            throw new IllegalArgumentException("Cannot transfer to the same account");
        }
        this.transactionId = transactionId;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.type = type;
        this.timestamp = timestamp;
    }

    public UUID getTransactionId() {
        return transactionId;
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

    public TransactionType getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(transactionId, that.transactionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId);
    }
}
