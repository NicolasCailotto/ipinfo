package com.mercadolibre.ipinfo.exception;

/**
 * Exception utilizada cuando una IP solicitada se encuentra en la black list
 */
public class IpAccessDeniedException extends RuntimeException {
    public IpAccessDeniedException(String message) {
        super(message);
    }
}
