package com.mercadolibre.ipinfo.exception;

/**
 * Exception utilizada cuando se encuentra un problema en las llamadas a servicios externos
 */
public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String message) {
        super(message);
    }
}
