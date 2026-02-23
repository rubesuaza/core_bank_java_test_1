package com.bank.core.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionTest {

    @Test
    void shouldCreateTransactionWithAllFields() {
        UUID transactionId = UUID.randomUUID();
        UUID sourceAccountId = UUID.randomUUID();
        UUID destinationAccountId = UUID.randomUUID();
        Money amount = new Money(new BigDecimal("100.50"), "USD");
        LocalDateTime timestamp = LocalDateTime.of(2025, 2, 23, 10, 30);

        Transaction transaction = new Transaction(
                transactionId,
                sourceAccountId,
                destinationAccountId,
                amount,
                TransactionType.TRANSFER,
                timestamp
        );

        assertThat(transaction.transactionId()).isEqualTo(transactionId);
        assertThat(transaction.sourceAccountId()).isEqualTo(sourceAccountId);
        assertThat(transaction.destinationAccountId()).isEqualTo(destinationAccountId);
        assertThat(transaction.amount()).isEqualTo(amount);
        assertThat(transaction.type()).isEqualTo(TransactionType.TRANSFER);
        assertThat(transaction.timestamp()).isEqualTo(timestamp);
    }

    @Test
    void shouldCreateDepositTransactionWithNullSource() {
        UUID transactionId = UUID.randomUUID();
        UUID destinationAccountId = UUID.randomUUID();
        Money amount = new Money(new BigDecimal("50"), "USD");
        LocalDateTime timestamp = LocalDateTime.now();

        Transaction transaction = new Transaction(
                transactionId,
                null,
                destinationAccountId,
                amount,
                TransactionType.DEPOSIT,
                timestamp
        );

        assertThat(transaction.sourceAccountId()).isNull();
        assertThat(transaction.destinationAccountId()).isEqualTo(destinationAccountId);
        assertThat(transaction.type()).isEqualTo(TransactionType.DEPOSIT);
    }

    @Test
    void shouldCreateWithdrawalTransactionWithNullDestination() {
        UUID transactionId = UUID.randomUUID();
        UUID sourceAccountId = UUID.randomUUID();
        Money amount = new Money(new BigDecimal("25"), "EUR");
        LocalDateTime timestamp = LocalDateTime.now();

        Transaction transaction = new Transaction(
                transactionId,
                sourceAccountId,
                null,
                amount,
                TransactionType.WITHDRAWAL,
                timestamp
        );

        assertThat(transaction.sourceAccountId()).isEqualTo(sourceAccountId);
        assertThat(transaction.destinationAccountId()).isNull();
        assertThat(transaction.type()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(transaction.amount().currency()).isEqualTo("EUR");
    }
}
