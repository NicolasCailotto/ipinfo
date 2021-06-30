package com.mercadolibre.ipinfo.dao;

import com.mercadolibre.ipinfo.model.fixerService.CurrencyData;

import java.util.Optional;

public interface CurrencyDataDAO {
    Optional<CurrencyData> getCurrencyDataByCurrencyCode(String currencyCode);
}
