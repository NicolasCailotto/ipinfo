package com.mercadolibre.ipinfo.exception;

/**
 * Exception utilizada cuando una IP solicitada para ser baneada ya se encuentra en la DB
 */
public class BannedIpExistException extends RuntimeException {
    public BannedIpExistException(String message) {
        super(message);
    }
}
