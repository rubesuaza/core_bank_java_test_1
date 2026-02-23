package com.bank.core.domain.exception;

/**
 * Excepción lanzada cuando se intenta operar entre diferentes divisas.
 */
public class CurrencyMismatchException extends DomainException {

    public CurrencyMismatchException(String message) {
        super(message);
    }
}
