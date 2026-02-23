package com.bank.core.domain.exception;

/**
 * Excepción base para todas las excepciones de dominio.
 * Las reglas de negocio que se violan deben lanzar subclases de esta excepción.
 */
public abstract class DomainException extends RuntimeException {

    protected DomainException(String message) {
        super(message);
    }

    protected DomainException(String message, Throwable cause) {
        super(message, cause);
    }
}
