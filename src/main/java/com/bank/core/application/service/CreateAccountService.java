package com.bank.core.application.service;

import com.bank.core.application.port.in.AccountCreatedResult;
import com.bank.core.application.port.in.CreateAccountCommand;
import com.bank.core.application.port.in.CreateAccountUseCase;
import com.bank.core.domain.exception.InvalidAmountException;
import com.bank.core.domain.model.Account;
import com.bank.core.domain.model.AccountStatus;
import com.bank.core.domain.model.Money;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

/**
 * Implementación del caso de uso de creación de cuentas.
 */
public class CreateAccountService implements CreateAccountUseCase {

    @Override
    public AccountCreatedResult createAccount(CreateAccountCommand command) {
        Objects.requireNonNull(command, "command must not be null");

        if (command.getInitialBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidAmountException("Initial balance must be zero or positive");
        }

        UUID accountId = UUID.randomUUID();
        String accountNumber = "ACC-" + accountId.toString().substring(0, 8);

        Money initialMoney = Money.of(command.getInitialBalance(), command.getCurrency());
        UUID ownerId = command.getOwnerId();

        // En este punto normalmente se persistiría la entidad Account en un repositorio.
        // TODO(user): Inyectar y utilizar un puerto de salida (repositorio) para persistir la cuenta creada.
        new Account(accountId, accountNumber, initialMoney, ownerId, AccountStatus.ACTIVE);

        return new AccountCreatedResult(
                accountId,
                accountNumber,
                AccountStatus.ACTIVE.name(),
                initialMoney.getAmount(),
                initialMoney.getCurrency()
        );
    }
}

