package com.bank.core.domain.exception;

/**
 * Se lanza cuando se intenta operar sobre una cuenta que no está en estado ACTIVE.
 */
public class InactiveAccountException extends DomainException {

    public InactiveAccountException(String message) {
        super(message);
    }
}
