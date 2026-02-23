package com.bank.core.domain.exception;

/**
 * Excepción lanzada cuando el monto es negativo o cero.
 */
public class InvalidAmountException extends DomainException {

    public InvalidAmountException(String message) {
        super(message);
    }
}
