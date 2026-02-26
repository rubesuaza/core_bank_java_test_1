package com.bank.core.domain.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.bank.core.domain.exception.CurrencyMismatchException;
import com.bank.core.domain.exception.InactiveAccountException;
import com.bank.core.domain.exception.InsufficientFundsException;
import com.bank.core.domain.exception.InvalidAmountException;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class AccountTest {

    @Test
    void depositIntoActiveAccountIncreasesBalance() {
        Account account = new Account(
                UUID.randomUUID(),
                "ACC-001",
                Money.of(new BigDecimal("100.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );

        account.credit(Money.of(new BigDecimal("50.00"), "USD"));

        assertThat(account.getBalance().getAmount()).isEqualByComparingTo("150.00");
    }

    @Test
    void depositOnInactiveAccountShouldThrow() {
        Account frozenAccount = new Account(
                UUID.randomUUID(),
                "ACC-002",
                Money.of(new BigDecimal("100.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.FROZEN
        );

        assertThatThrownBy(() -> frozenAccount.credit(Money.of(new BigDecimal("10.00"), "USD")))
                .isInstanceOf(InactiveAccountException.class);
    }

    @Test
    void withdrawWithSufficientFundsDecreasesBalance() {
        Account account = new Account(
                UUID.randomUUID(),
                "ACC-003",
                Money.of(new BigDecimal("100.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );

        account.debit(Money.of(new BigDecimal("40.00"), "USD"));

        assertThat(account.getBalance().getAmount()).isEqualByComparingTo("60.00");
    }

    @Test
    void withdrawWithInsufficientFundsShouldThrow() {
        Account account = new Account(
                UUID.randomUUID(),
                "ACC-004",
                Money.of(new BigDecimal("50.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );

        assertThatThrownBy(() -> account.debit(Money.of(new BigDecimal("60.00"), "USD")))
                .isInstanceOf(InsufficientFundsException.class);
    }

    @Test
    void withdrawWithNonPositiveAmountShouldThrowInvalidAmount() {
        Account account = new Account(
                UUID.randomUUID(),
                "ACC-005",
                Money.of(new BigDecimal("50.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );

        assertThatThrownBy(() -> account.debit(Money.of(new BigDecimal("0.00"), "USD")))
                .isInstanceOf(InvalidAmountException.class);

        assertThatThrownBy(() -> account.debit(Money.of(new BigDecimal("-10.00"), "USD")))
                .isInstanceOf(InvalidAmountException.class);
    }

    @Test
    void depositWithCurrencyMismatchShouldThrow() {
        Account account = new Account(
                UUID.randomUUID(),
                "ACC-006",
                Money.of(new BigDecimal("100.00"), "USD"),
                UUID.randomUUID(),
                AccountStatus.ACTIVE
        );

        assertThatThrownBy(() -> account.credit(Money.of(new BigDecimal("10.00"), "EUR")))
                .isInstanceOf(CurrencyMismatchException.class);
    }
}

