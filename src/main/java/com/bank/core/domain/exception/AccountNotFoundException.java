package com.bank.core.domain.exception;

import java.util.UUID;

/**
 * Se lanza cuando no se encuentra una cuenta por su identificador.
 */
public class AccountNotFoundException extends DomainException {

    public AccountNotFoundException(UUID accountId) {
        super("Account not found: " + accountId);
    }

    public AccountNotFoundException(String message) {
        super(message);
    }
}
