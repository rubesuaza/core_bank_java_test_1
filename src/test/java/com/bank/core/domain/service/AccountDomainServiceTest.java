package com.bank.core.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bank.core.domain.exception.CurrencyMismatchException;
import com.bank.core.domain.exception.InactiveAccountException;
import com.bank.core.domain.exception.InsufficientFundsException;
import com.bank.core.domain.exception.InvalidAmountException;
import com.bank.core.domain.exception.DomainException;
import com.bank.core.domain.model.Account;
import com.bank.core.domain.model.AccountStatus;
import com.bank.core.domain.model.Money;
import com.bank.core.domain.model.Transaction;
import com.bank.core.domain.model.TransactionType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AccountDomainServiceTest {

    private final AccountDomainService service = new AccountDomainService();

    @Test
    void depositCreatesTransactionAndUpdatesBalance() {
        Account account = new Account(
                UUID.randomUUID(),
                "ACC-100",
                Money.of(new BigDecimal("100.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );
        Money amount = Money.of(new BigDecimal("25.00"), "USD");
        LocalDateTime now = LocalDateTime.now();

        Transaction tx = service.deposit(account, amount, now);

        assertThat(tx.getType()).isEqualTo(TransactionType.DEPOSIT);
        assertThat(tx.getSourceAccountId()).isNull();
        assertThat(tx.getDestinationAccountId()).isEqualTo(account.getAccountId());
        assertThat(tx.getAmount().getAmount()).isEqualByComparingTo("25.00");
        assertThat(tx.getTimestamp()).isEqualTo(now);
        assertThat(account.getBalance().getAmount()).isEqualByComparingTo("125.00");
    }

    @Test
    void withdrawCreatesTransactionAndUpdatesBalance() {
        Account account = new Account(
                UUID.randomUUID(),
                "ACC-101",
                Money.of(new BigDecimal("100.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );
        Money amount = Money.of(new BigDecimal("40.00"), "USD");
        LocalDateTime now = LocalDateTime.now();

        Transaction tx = service.withdraw(account, amount, now);

        assertThat(tx.getType()).isEqualTo(TransactionType.WITHDRAWAL);
        assertThat(tx.getSourceAccountId()).isEqualTo(account.getAccountId());
        assertThat(tx.getDestinationAccountId()).isNull();
        assertThat(tx.getAmount().getAmount()).isEqualByComparingTo("40.00");
        assertThat(tx.getTimestamp()).isEqualTo(now);
        assertThat(account.getBalance().getAmount()).isEqualByComparingTo("60.00");
    }

    @Test
    void transferMovesFundsBetweenAccountsAndCreatesTransaction() {
        Account source = new Account(
                UUID.randomUUID(),
                "ACC-200",
                Money.of(new BigDecimal("100.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );
        Account destination = new Account(
                UUID.randomUUID(),
                "ACC-201",
                Money.of(new BigDecimal("50.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );
        Money amount = Money.of(new BigDecimal("30.00"), "USD");
        LocalDateTime now = LocalDateTime.now();

        Transaction tx = service.transfer(source, destination, amount, now);

        assertThat(tx.getType()).isEqualTo(TransactionType.TRANSFER);
        assertThat(tx.getSourceAccountId()).isEqualTo(source.getAccountId());
        assertThat(tx.getDestinationAccountId()).isEqualTo(destination.getAccountId());
        assertThat(tx.getAmount().getAmount()).isEqualByComparingTo("30.00");
        assertThat(tx.getTimestamp()).isEqualTo(now);

        assertThat(source.getBalance().getAmount()).isEqualByComparingTo("70.00");
        assertThat(destination.getBalance().getAmount()).isEqualByComparingTo("80.00");
    }

    @Test
    void transferWithSameSourceAndDestinationShouldThrowDomainException() {
        Account account = new Account(
                UUID.randomUUID(),
                "ACC-300",
                Money.of(new BigDecimal("100.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );
        Money amount = Money.of(new BigDecimal("10.00"), "USD");

        assertThatThrownBy(() -> service.transfer(account, account, amount, LocalDateTime.now()))
                .isInstanceOf(DomainException.class);
    }

    @Test
    void transferWithInactiveSourceOrDestinationShouldThrowInactiveAccountException() {
        Account active = new Account(
                UUID.randomUUID(),
                "ACC-400",
                Money.of(new BigDecimal("100.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );
        Account frozen = new Account(
                UUID.randomUUID(),
                "ACC-401",
                Money.of(new BigDecimal("50.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.FROZEN
        );
        Money amount = Money.of(new BigDecimal("10.00"), "USD");

        assertThatThrownBy(() -> service.transfer(frozen, active, amount, LocalDateTime.now()))
                .isInstanceOf(InactiveAccountException.class);

        assertThatThrownBy(() -> service.transfer(active, frozen, amount, LocalDateTime.now()))
                .isInstanceOf(InactiveAccountException.class);
    }

    @Test
    void transferWithCurrencyMismatchShouldThrow() {
        Account usdAccount = new Account(
                UUID.randomUUID(),
                "ACC-500",
                Money.of(new BigDecimal("100.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );
        Account eurAccount = new Account(
                UUID.randomUUID(),
                "ACC-501",
                Money.of(new BigDecimal("50.00"), "EUR"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );
        Money amountInUsd = Money.of(new BigDecimal("10.00"), "USD");

        assertThatThrownBy(() -> service.transfer(usdAccount, eurAccount, amountInUsd, LocalDateTime.now()))
                .isInstanceOf(CurrencyMismatchException.class);
    }

    @Test
    void transferWithNonPositiveAmountShouldThrowInvalidAmount() {
        Account source = new Account(
                UUID.randomUUID(),
                "ACC-600",
                Money.of(new BigDecimal("100.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );
        Account destination = new Account(
                UUID.randomUUID(),
                "ACC-601",
                Money.of(new BigDecimal("50.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );

        Money zero = Money.of(new BigDecimal("0.00"), "USD");
        Money negative = Money.of(new BigDecimal("-5.00"), "USD");

        assertThatThrownBy(() -> service.transfer(source, destination, zero, LocalDateTime.now()))
                .isInstanceOf(InvalidAmountException.class);

        assertThatThrownBy(() -> service.transfer(source, destination, negative, LocalDateTime.now()))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void transferWithInsufficientFundsShouldThrowInsufficientFundsException() {
        Account source = new Account(
                UUID.randomUUID(),
                "ACC-700",
                Money.of(new BigDecimal("20.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );
        Account destination = new Account(
                UUID.randomUUID(),
                "ACC-701",
                Money.of(new BigDecimal("50.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );
        Money amount = Money.of(new BigDecimal("30.00"), "USD");

        assertThatThrownBy(() -> service.transfer(source, destination, amount, LocalDateTime.now()))
                .isInstanceOf(InsufficientFundsException.class);
    }
}

