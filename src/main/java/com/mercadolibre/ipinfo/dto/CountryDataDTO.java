package com.mercadolibre.ipinfo.dto;

import com.mercadolibre.ipinfo.model.restCountriesService.CountryData;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Data
public class CountryDataDTO implements Serializable {

    public String name;

    public String countryCode;

    public List<CurrencyDTO> currencies;

    public CountryDataDTO() {
        this.currencies = Collections.emptyList();
    }

    public CountryDataDTO(CountryData countryData) {
        this.name = countryData.getName();
        this.countryCode = countryData.getAlpha3Code();
        this.currencies = countryData.getCurrencies().stream()
                .map(CurrencyDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "CountryDataDTO{" +
                "name='" + name + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", currencies=" + currencies +
                '}';
    }
}
