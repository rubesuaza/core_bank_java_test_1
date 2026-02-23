package com.bank.core.domain.exception;

/**
 * Se lanza cuando se intenta operar entre divisas diferentes.
 */
public class CurrencyMismatchException extends DomainException {

    public CurrencyMismatchException(String message) {
        super(message);
    }
}
