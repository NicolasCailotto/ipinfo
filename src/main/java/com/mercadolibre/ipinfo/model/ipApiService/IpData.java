package com.mercadolibre.ipinfo.model.ipApiService;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * Clase que representa el objeto de respuesta de la api externa IpData
 * Solo se mapean los datos que son necesarions, los demas son ignorados.
 * Con la annotation @JsonProperty se especifica a Jackson cual es el campo del Json recibido que se debe setear
 * en el atributo
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IpData implements Serializable {

    public String ip;

    @JsonProperty("country_code")
    public String countryCode;

    @JsonProperty("country_name")
    public String countryName;

    @Override
    public String toString() {
        return "IpData{" +
                "ip='" + ip + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                '}';
    }
}
