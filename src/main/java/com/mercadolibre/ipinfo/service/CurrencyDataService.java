package com.mercadolibre.ipinfo.service;

import com.mercadolibre.ipinfo.dto.CurrencyDTO;
import com.mercadolibre.ipinfo.dto.CurrencyDataDTO;

import java.util.List;

public interface CurrencyDataService {
    CurrencyDataDTO getCurrencyDataByCurrencyCode(String currencyCode);

    List<CurrencyDataDTO> getCurrencyDataByCurrenciesCodes(List<CurrencyDTO> currencies);
}
