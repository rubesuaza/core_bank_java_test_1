package com.bank.core.domain.exception;

/**
 * Excepción lanzada cuando los fondos son menores al monto solicitado.
 */
public class InsufficientFundsException extends DomainException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}
