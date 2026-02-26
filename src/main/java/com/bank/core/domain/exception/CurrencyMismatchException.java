package com.bank.core.domain.exception;

/**
 * Se lanza cuando se intenta operar con montos de distinta moneda.
 */
public class CurrencyMismatchException extends DomainException {

    public CurrencyMismatchException(String message) {
        super(message);
    }
}

