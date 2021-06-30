package com.mercadolibre.ipinfo.service;

import com.mercadolibre.ipinfo.dto.CountryDataDTO;

public interface CountryDataService {
    CountryDataDTO getCountryDataByCountryCode(String countryCode);
}
