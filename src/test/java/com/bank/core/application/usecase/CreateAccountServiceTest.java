package com.bank.core.application.usecase;

import com.bank.core.application.port.output.AccountNumberGenerator;
import com.bank.core.application.port.output.AccountRepository;
import com.bank.core.domain.model.Account;
import com.bank.core.domain.model.AccountStatus;
import com.bank.core.domain.model.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateAccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountNumberGenerator accountNumberGenerator;

    private CreateAccountService createAccountService;

    @BeforeEach
    void setUp() {
        createAccountService = new CreateAccountService(accountRepository, accountNumberGenerator);
    }

    @Test
    void shouldCreateAccountWithGeneratedNumberAndSaveToRepository() {
        // Arrange
        UUID ownerId = UUID.randomUUID();
        BigDecimal initialBalance = new BigDecimal("100.50");
        String currency = "USD";
        String generatedAccountNumber = "ACC12345";

        when(accountNumberGenerator.generate()).thenReturn(generatedAccountNumber);
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Account result = createAccountService.create(ownerId, initialBalance, currency);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getAccountNumber()).isEqualTo(generatedAccountNumber);
        assertThat(result.getOwnerId()).isEqualTo(ownerId);
        assertThat(result.getStatus()).isEqualTo(AccountStatus.ACTIVE);
        assertThat(result.getBalance().getAmount()).isEqualByComparingTo("100.50");
        assertThat(result.getBalance().getCurrency()).isEqualTo("USD");

        verify(accountNumberGenerator).generate();
        verify(accountRepository).save(result);
    }

    @Test
    void shouldCreateAccountWithZeroInitialBalance() {
        // Arrange
        UUID ownerId = UUID.randomUUID();
        when(accountNumberGenerator.generate()).thenReturn("ZERO0001");
        when(accountRepository.save(any(Account.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Account result = createAccountService.create(ownerId, BigDecimal.ZERO, "EUR");

        // Assert
        assertThat(result.getBalance().getAmount()).isEqualByComparingTo("0");
        assertThat(result.getBalance().getCurrency()).isEqualTo("EUR");
    }

    @Test
    void shouldReturnSavedAccountFromRepository() {
        // Arrange
        UUID ownerId = UUID.randomUUID();
        Account savedAccount = new Account(
                UUID.randomUUID(),
                "SAVED01",
                new Money("200", "GBP"),
                ownerId,
                AccountStatus.ACTIVE
        );
        when(accountNumberGenerator.generate()).thenReturn("SAVED01");
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        // Act
        Account result = createAccountService.create(ownerId, new BigDecimal("200"), "GBP");

        // Assert
        assertThat(result).isSameAs(savedAccount);
    }
}
