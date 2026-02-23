package com.bank.core.domain.exception;

/**
 * Clase base para todas las excepciones de dominio.
 * El dominio no depende de frameworks externos.
 */
public abstract class DomainException extends RuntimeException {

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
