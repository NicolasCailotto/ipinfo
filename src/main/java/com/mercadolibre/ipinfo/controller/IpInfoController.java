package com.mercadolibre.ipinfo.controller;

import com.mercadolibre.ipinfo.controller.response.ErrorResponse;
import com.mercadolibre.ipinfo.controller.response.IpInformationDTO;
import com.mercadolibre.ipinfo.exception.InvalidIpFormatException;
import com.mercadolibre.ipinfo.service.IpInfoService;
import com.mercadolibre.ipinfo.utils.IpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest Controller que contiene el endpoint especifico de la api para obtener la informacion de la IP que llega
 * como pathParam
 */
@RestController
@RequestMapping("v1/api")
public class IpInfoController {

    private IpInfoService ipInfoService;

    public IpInfoController(IpInfoService ipInfoService) {
        this.ipInfoService = ipInfoService;
    }

    @GetMapping("/{ip}/info")
    @Operation(summary = "Obtiene informacion de la IP solicitada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the IP information",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = IpInformationDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid IP supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "403", description = "IP can't access to api endpoint",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})})
    public ResponseEntity<IpInformationDTO> ipInfo(@PathVariable("ip") final String ip) {
        // Se valida que el path param sea un ip valida, caso contrario se devuelve un bad request
        if (!IpUtils.isValidIp(ip)) {
            throw new InvalidIpFormatException("El formato de la direccion IP es incorrecto");
        }
        return ResponseEntity.ok(ipInfoService.getIpInfo(ip));
    }

}
