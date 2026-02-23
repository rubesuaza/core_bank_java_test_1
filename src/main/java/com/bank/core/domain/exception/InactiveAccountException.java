package com.bank.core.domain.exception;

/**
 * Excepción lanzada cuando se intenta operar sobre una cuenta no activa.
 */
public class InactiveAccountException extends DomainException {

    public InactiveAccountException(String message) {
        super(message);
    }
}
