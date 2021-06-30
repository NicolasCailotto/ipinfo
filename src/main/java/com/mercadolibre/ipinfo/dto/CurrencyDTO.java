package com.mercadolibre.ipinfo.dto;

import com.mercadolibre.ipinfo.model.restCountriesService.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDTO implements Serializable {

    public String code;

    public String name;

    public CurrencyDTO(Currency currency) {
        this.code = currency.getCode();
        this.name = currency.getName();
    }

    @Override
    public String toString() {
        return "CurrencyDTO{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
