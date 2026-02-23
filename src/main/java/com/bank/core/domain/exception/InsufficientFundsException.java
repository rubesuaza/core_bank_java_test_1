package com.bank.core.domain.exception;

/**
 * Se lanza cuando los fondos disponibles son menores al monto solicitado.
 */
public class InsufficientFundsException extends DomainException {

    public InsufficientFundsException(String message) {
        super(message);
    }
}
