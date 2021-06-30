package com.mercadolibre.ipinfo.dao;

import com.mercadolibre.ipinfo.model.restCountriesService.CountryData;

import java.util.Optional;

public interface CountryDataDAO {

    Optional<CountryData> getCountryDataByCountryCode(String countryCode);
}
