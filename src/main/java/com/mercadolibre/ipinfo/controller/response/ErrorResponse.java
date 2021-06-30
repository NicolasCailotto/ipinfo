package com.mercadolibre.ipinfo.controller.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase para representar una respuesta con error por parte de la api
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Integer code;

    private String msg;
}
