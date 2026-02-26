package com.bank.core.domain.exception;

/**
 * Se lanza cuando una cuenta no tiene fondos suficientes para una operación.
 */
public class InsufficientFundsException extends DomainException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}

