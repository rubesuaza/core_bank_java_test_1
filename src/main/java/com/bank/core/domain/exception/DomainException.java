package com.bank.core.domain.exception;

/**
 * Excepción base para errores de dominio en el core bancario.
 */
public class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}

