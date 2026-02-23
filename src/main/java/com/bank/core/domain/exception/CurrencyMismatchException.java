package com.bank.core.domain.exception;

/**
 * Se lanza cuando se intenta una operación entre diferentes divisas.
 */
public class CurrencyMismatchException extends DomainException {

    public CurrencyMismatchException(String message) {
        super(message);
    }
}
