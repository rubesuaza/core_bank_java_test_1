package com.bank.core.domain.exception;

/**
 * Se lanza cuando un monto no es válido (por ejemplo, cero o negativo).
 */
public class InvalidAmountException extends DomainException {

    public InvalidAmountException(String message) {
        super(message);
    }
}

