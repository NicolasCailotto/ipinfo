package com.mercadolibre.ipinfo.exception;

/**
 * Exception utilizada cuando una la IP del request no tiene un formato valido
 */
public class InvalidIpFormatException extends RuntimeException {
    public InvalidIpFormatException(String message) {
        super(message);
    }
}
