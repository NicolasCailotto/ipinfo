package com.mercadolibre.ipinfo.service.impl;

import com.mercadolibre.ipinfo.config.cache.CacheNames;
import com.mercadolibre.ipinfo.dao.CurrencyDataDAO;
import com.mercadolibre.ipinfo.dto.CurrencyDTO;
import com.mercadolibre.ipinfo.dto.CurrencyDataDTO;
import com.mercadolibre.ipinfo.model.fixerService.CurrencyData;
import com.mercadolibre.ipinfo.service.CurrencyDataService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Esta clase contiene la logica para obtener la informacion especifica de una moenda
 * desde la capa de DAO
 */
@Service
public class CurrencyDataServiceImpl implements CurrencyDataService {

    /**
     * The CurrencyDataDAO
     * El dao injectado en este caso es el que obtiene la info desde un api externa
     */
    private CurrencyDataDAO currencyDataDAO;

    public CurrencyDataServiceImpl(@Qualifier("fixerApiService") CurrencyDataDAO currencyDataDAO) {
        this.currencyDataDAO = currencyDataDAO;
    }

    /**
     * Este metodo transforma la data recibida desde la capa DAO al DTO de la logica de negocio
     * Este paso es necesario debido a que la data obtenida desde la api externa es en base a la moneda EUR
     * por lo tanto se tiene que transformar para que los datos que se muestran queden en base a la moneda solicitada
     *
     * @param currencyData
     * @param currencyCode
     * @return
     */
    private CurrencyDataDTO transformToDTO(CurrencyData currencyData, String currencyCode) {
        CurrencyDataDTO currencyDataDTO = new CurrencyDataDTO();
        currencyDataDTO.setCode(currencyCode);

        Map<String, BigDecimal> ratesEURBase = currencyData.getRates();

        BigDecimal currencyValueInEUR = ratesEURBase.get(currencyCode);
        HashMap<String, BigDecimal> newRates = new HashMap<>();
        ratesEURBase.forEach((code, rateValue) -> {
            if (code.equalsIgnoreCase(currencyCode)) {
                newRates.computeIfAbsent(currencyData.getBase(), v -> BigDecimal.ONE.divide(rateValue, 5, RoundingMode.HALF_UP));
            } else {
                newRates.compute(code, (s, oldValue) -> rateValue.divide(currencyValueInEUR, 5, RoundingMode.HALF_UP));
            }
        });

        currencyDataDTO.setRates(newRates);
        return currencyDataDTO;
    }

    /**
     * Metodo que consulta la capa DAO a partir de una sola moneda
     *
     * @param currencyCode
     * @return
     */
    @Override
    public CurrencyDataDTO getCurrencyDataByCurrencyCode(String currencyCode) {
        if (currencyCode == null || "".equalsIgnoreCase(currencyCode)) {
            return new CurrencyDataDTO();
        }
        Optional<CurrencyData> currencyData = currencyDataDAO.getCurrencyDataByCurrencyCode(currencyCode);
        return currencyData.map(data -> transformToDTO(data, currencyCode)).orElse(new CurrencyDataDTO());
    }

    /**
     * Metodo que consulta la capa DAO para cada una de las monedas recibidas en la lista.
     * Con la annotation @Cacheble especificamos el la respuesta de este metodo se cachee para no tener que volver a
     * consultarla reiteradas veces
     *
     * @param currencies
     * @return
     */
    @Override
    @Cacheable(CacheNames.CURRENCY_DATA_CACHE)
    public List<CurrencyDataDTO> getCurrencyDataByCurrenciesCodes(List<CurrencyDTO> currencies) {
        return currencies.stream()
                .map(currencyDTO -> getCurrencyDataByCurrencyCode(currencyDTO.getCode()))
                .collect(Collectors.toList());
    }
}
