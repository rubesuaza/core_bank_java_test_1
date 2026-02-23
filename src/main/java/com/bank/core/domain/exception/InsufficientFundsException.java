package com.bank.core.domain.exception;

/**
 * Se lanza cuando el balance de la cuenta es menor al monto solicitado.
 */
public class InsufficientFundsException extends DomainException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}
