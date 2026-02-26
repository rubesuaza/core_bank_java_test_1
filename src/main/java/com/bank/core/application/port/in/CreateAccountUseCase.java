package com.bank.core.application.port.in;

/**
 * Puerto de entrada para la creación de cuentas.
 */
public interface CreateAccountUseCase {

    AccountCreatedResult createAccount(CreateAccountCommand command);
}

