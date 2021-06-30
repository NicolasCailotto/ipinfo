package com.mercadolibre.ipinfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
public class CurrencyDataDTO implements Serializable {
    public String code;
    public Map<String, BigDecimal> rates;

    public CurrencyDataDTO() {
        this.rates = new HashMap<>();
    }

    @Override
    public String toString() {
        return "CurrencyDataDTO{" +
                "code='" + code + '\'' +
                ", rates=" + rates +
                '}';
    }
}
