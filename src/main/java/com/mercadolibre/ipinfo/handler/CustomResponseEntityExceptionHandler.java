package com.mercadolibre.ipinfo.handler;

import com.mercadolibre.ipinfo.controller.response.ErrorResponse;
import com.mercadolibre.ipinfo.exception.BannedIpExistException;
import com.mercadolibre.ipinfo.exception.ExternalServiceException;
import com.mercadolibre.ipinfo.exception.InvalidIpFormatException;
import com.mercadolibre.ipinfo.exception.IpAccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Clase con la configuracion de los distintos ExceptionHandlers que se tienen en la aplicacion
 * Esto es usado para retornar automaticamente una respuesta especifica cuando se encuentran distintas
 * excepciones
 */
@ControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = ExternalServiceException.class)
    protected ResponseEntity<ErrorResponse> handlerError(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = IpAccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handlerIpAccessDeniedException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = InvalidIpFormatException.class)
    protected ResponseEntity<ErrorResponse> handlerInvalidIpFormatException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
    }

    @ExceptionHandler(value = BannedIpExistException.class)
    protected ResponseEntity<ErrorResponse> handlerBannedIpExistException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()));
    }
}