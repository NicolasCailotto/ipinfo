package com.mercadolibre.ipinfo.controller;

import com.mercadolibre.ipinfo.controller.request.BannedIp;
import com.mercadolibre.ipinfo.controller.response.ErrorResponse;
import com.mercadolibre.ipinfo.controller.response.IpInformationDTO;
import com.mercadolibre.ipinfo.exception.InvalidIpFormatException;
import com.mercadolibre.ipinfo.service.IpService;
import com.mercadolibre.ipinfo.utils.IpUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * Rest Controller que contiene el endpoint especifico de la api para obtener la informacion de la IP que llega
 * como pathParam
 */
@RestController
@RequestMapping("v1/api")
public class IpInfoController {

    private IpService ipService;

    public IpInfoController(IpService ipService) {
        this.ipService = ipService;
    }

    @GetMapping("/ip/{ip}")
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
        validateIp(ip);
        return ResponseEntity.ok(ipService.getIpInfo(ip));
    }

    @PostMapping(value = "/ip", consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(summary = "Pone la IP recibida en el body en una lista de baneadas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "La IP pudo ser baneada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = BannedIp.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid IP supplied",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))}),
            @ApiResponse(responseCode = "403", description = "IP can't access to api endpoint",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class))})})
    public ResponseEntity<BannedIp> banIp(@Valid @RequestBody BannedIp ip) {
        // Se valida que el path param sea un ip valida, caso contrario se devuelve un bad request
        validateIp(ip.getIpAddress());
        return ResponseEntity.ok(ipService.banIp(ip));
    }

    private void validateIp(String ip) {
        if (!IpUtils.isValidIp(ip)) {
            throw new InvalidIpFormatException("El formato de la direccion IP es incorrecto");
        }
    }
}
