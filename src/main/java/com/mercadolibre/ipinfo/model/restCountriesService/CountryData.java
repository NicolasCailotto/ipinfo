package com.mercadolibre.ipinfo.model.restCountriesService;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Clase que representa el objeto de respuesta de la api externa RestCountries
 * Solo se mapean los datos que son necesarions, los demas son ignorados.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryData implements Serializable {

    public String name;

    public String alpha3Code;

    public List<Currency> currencies;

    @Override
    public String toString() {
        return "CountryData{" +
                "name='" + name + '\'' +
                ", alpha3Code='" + alpha3Code + '\'' +
                ", currencies=" + currencies +
                '}';
    }
}
