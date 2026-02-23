package com.bank.core.domain.exception;

/**
 * Se lanza cuando el monto de una transacción es negativo o cero.
 */
public class InvalidAmountException extends DomainException {

    public InvalidAmountException(String message) {
        super(message);
    }
}
