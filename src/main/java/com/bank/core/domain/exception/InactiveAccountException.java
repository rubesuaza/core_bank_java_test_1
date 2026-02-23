package com.bank.core.domain.exception;

/**
 * Se lanza cuando se intenta realizar una operación sobre una cuenta no activa.
 */
public class InactiveAccountException extends DomainException {

    public InactiveAccountException(String message) {
        super(message);
    }
}
