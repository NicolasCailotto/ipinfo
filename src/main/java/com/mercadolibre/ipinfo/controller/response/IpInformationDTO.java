package com.mercadolibre.ipinfo.controller.response;

import com.mercadolibre.ipinfo.dto.CurrencyDataDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Clase para representar una respuesta con la informacion que se devuelve en la api
 */
@Data
public class IpInformationDTO implements Serializable {

    private String ip;

    private String countryName;

    private String countryCode;

    private List<CurrencyDataDTO> currencies;

    @Override
    public String toString() {
        return "IpInformationDTO{" +
                "ip='" + ip + '\'' +
                ", countryName='" + countryName + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", currencies=" + currencies +
                '}';
    }
}
