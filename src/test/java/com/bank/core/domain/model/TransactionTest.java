package com.bank.core.domain.model;

import com.bank.core.domain.exception.InvalidAmountException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TransactionTest {

    @Test
    void shouldCreateDepositTransaction() {
        UUID sourceId = UUID.randomUUID();
        Money amount = new Money("100.00", "USD");
        Transaction tx = new Transaction(
                UUID.randomUUID(),
                sourceId,
                null,
                amount,
                TransactionType.DEPOSIT,
                LocalDateTime.now()
        );
        assertThat(tx.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(tx.getAmount()).isEqualTo(amount);
        assertThat(tx.getSourceAccountId()).isEqualTo(sourceId);
        assertThat(tx.getDestinationAccountId()).isNull();
    }

    @Test
    void shouldCreateTransferTransaction() {
        UUID sourceId = UUID.randomUUID();
        UUID destId = UUID.randomUUID();
        Money amount = new Money("50.00", "USD");
        Transaction tx = new Transaction(
                UUID.randomUUID(),
                sourceId,
                destId,
                amount,
                TransactionType.TRANSFER,
                LocalDateTime.now()
        );
        assertThat(tx.getType()).isEqualTo(TransactionType.TRANSFER);
        assertThat(tx.getDestinationAccountId()).isEqualTo(destId);
    }

    @Test
    void shouldThrowInvalidAmountExceptionWhenAmountIsZero() {
        assertThatThrownBy(() -> new Transaction(
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                new Money(BigDecimal.ZERO, "USD"),
                TransactionType.DEPOSIT,
                LocalDateTime.now()
        )).isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void shouldThrowInvalidAmountExceptionWhenAmountIsNegative() {
        assertThatThrownBy(() -> new Transaction(
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                new Money("-10", "USD"),
                TransactionType.DEPOSIT,
                LocalDateTime.now()
        )).isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void shouldThrowWhenTransferToSameAccount() {
        UUID sameId = UUID.randomUUID();
        assertThatThrownBy(() -> new Transaction(
                UUID.randomUUID(),
                sameId,
                sameId,
                new Money("10", "USD"),
                TransactionType.TRANSFER,
                LocalDateTime.now()
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("same account");
    }

    @Test
    void shouldThrowWhenTransferWithoutDestination() {
        assertThatThrownBy(() -> new Transaction(
                UUID.randomUUID(),
                UUID.randomUUID(),
                null,
                new Money("10", "USD"),
                TransactionType.TRANSFER,
                LocalDateTime.now()
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("destinationAccountId");
    }
}
