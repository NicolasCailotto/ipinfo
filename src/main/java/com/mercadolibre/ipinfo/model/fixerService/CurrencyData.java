package com.mercadolibre.ipinfo.model.fixerService;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Clase que representa el objeto de respuesta de la api externa Fixer
 * Solo se mapean los datos que son necesarions, los demas son ignorados
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyData implements Serializable {
    public String base;
    public Map<String, BigDecimal> rates;

    @Override
    public String toString() {
        return "CurrencyData{" +
                "base='" + base + '\'' +
                ", rates=" + rates +
                '}';
    }
}
