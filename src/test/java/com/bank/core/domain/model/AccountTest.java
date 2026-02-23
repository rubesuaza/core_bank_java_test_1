package com.bank.core.domain.model;

import com.bank.core.domain.exception.CurrencyMismatchException;
import com.bank.core.domain.exception.InactiveAccountException;
import com.bank.core.domain.exception.InsufficientFundsException;
import com.bank.core.domain.exception.InvalidAmountException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class AccountTest {

    private UUID accountId;
    private UUID ownerId;
    private Account activeAccount;

    @BeforeEach
    void setUp() {
        accountId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
        activeAccount = new Account(
                accountId,
                "ACC-001",
                new Money(BigDecimal.ZERO, "USD"),
                ownerId,
                AccountStatus.ACTIVE
        );
    }

    @Test
    void shouldDepositMoneyWhenAccountIsActive() {
        Money amount = new Money("100.00", "USD");
        activeAccount.deposit(amount);
        assertThat(activeAccount.getBalance().getAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    void shouldWithdrawMoneyWhenSufficientFunds() {
        activeAccount.deposit(new Money("100.00", "USD"));
        activeAccount.withdraw(new Money("30.00", "USD"));
        assertThat(activeAccount.getBalance().getAmount()).isEqualByComparingTo("70.00");
    }

    @Test
    void shouldThrowInsufficientFundsExceptionWhenWithdrawingMoreThanBalance() {
        activeAccount.deposit(new Money("50.00", "USD"));
        assertThatThrownBy(() -> activeAccount.withdraw(new Money("100.00", "USD")))
                .isInstanceOf(InsufficientFundsException.class)
                .hasMessageContaining("Insufficient funds");
    }

    @Test
    void shouldThrowInactiveAccountExceptionWhenDepositingOnFrozenAccount() {
        Account frozenAccount = new Account(
                UUID.randomUUID(), "ACC-002",
                new Money("0", "USD"), ownerId,
                AccountStatus.FROZEN
        );
        assertThatThrownBy(() -> frozenAccount.deposit(new Money("100.00", "USD")))
                .isInstanceOf(InactiveAccountException.class)
                .hasMessageContaining("not active");
    }

    @Test
    void shouldThrowInactiveAccountExceptionWhenWithdrawingFromClosedAccount() {
        Account closedAccount = new Account(
                UUID.randomUUID(), "ACC-003",
                new Money("100", "USD"), ownerId,
                AccountStatus.CLOSED
        );
        assertThatThrownBy(() -> closedAccount.withdraw(new Money("50.00", "USD")))
                .isInstanceOf(InactiveAccountException.class);
    }

    @Test
    void shouldThrowInvalidAmountExceptionWhenDepositAmountIsZero() {
        assertThatThrownBy(() -> activeAccount.deposit(new Money("0", "USD")))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessageContaining("strictly greater than zero");
    }

    @Test
    void shouldThrowInvalidAmountExceptionWhenWithdrawAmountIsZero() {
        activeAccount.deposit(new Money("100", "USD"));
        assertThatThrownBy(() -> activeAccount.withdraw(new Money("0", "USD")))
                .isInstanceOf(InvalidAmountException.class)
                .hasMessageContaining("strictly greater than zero");
    }

    @Test
    void shouldThrowCurrencyMismatchExceptionWhenDepositingDifferentCurrency() {
        assertThatThrownBy(() -> activeAccount.deposit(new Money("100.00", "EUR")))
                .isInstanceOf(CurrencyMismatchException.class)
                .hasMessageContaining("currency");
    }

    @Test
    void shouldTransferBetweenAccountsWhenBothActive() {
        Account source = new Account(
                UUID.randomUUID(), "ACC-SRC",
                new Money("200.00", "USD"), ownerId,
                AccountStatus.ACTIVE
        );
        Account dest = new Account(
                UUID.randomUUID(), "ACC-DST",
                new Money("0", "USD"), ownerId,
                AccountStatus.ACTIVE
        );
        source.transferTo(dest, new Money("75.00", "USD"));
        assertThat(source.getBalance().getAmount()).isEqualByComparingTo("125.00");
        assertThat(dest.getBalance().getAmount()).isEqualByComparingTo("75.00");
    }

    @Test
    void shouldThrowWhenTransferringToSameAccount() {
        assertThatThrownBy(() -> activeAccount.transferTo(activeAccount, new Money("10.00", "USD")))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("same account");
    }

    @Test
    void shouldThrowWhenTransferExceedsBalance() {
        Account dest = new Account(
                UUID.randomUUID(), "ACC-DST",
                new Money("0", "USD"), ownerId,
                AccountStatus.ACTIVE
        );
        activeAccount.deposit(new Money("50.00", "USD"));
        assertThatThrownBy(() -> activeAccount.transferTo(dest, new Money("100.00", "USD")))
                .isInstanceOf(InsufficientFundsException.class);
    }
}
