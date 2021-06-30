package com.mercadolibre.ipinfo.service.impl;

import com.mercadolibre.ipinfo.config.cache.CacheNames;
import com.mercadolibre.ipinfo.dao.CountryDataDAO;
import com.mercadolibre.ipinfo.dto.CountryDataDTO;
import com.mercadolibre.ipinfo.model.restCountriesService.CountryData;
import com.mercadolibre.ipinfo.service.CountryDataService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Esta clase contiene la logica para obtener la informacion especifica de un pais
 * desde la capa de DAO
 */
@Service
public class CountryDataServiceImpl implements CountryDataService {

    /**
     * The CountryDataDAO
     * El dao injectado en este caso es el que obtiene la info desde un api externa
     */
    private CountryDataDAO countryDataDAO;

    public CountryDataServiceImpl(@Qualifier("restCountriesApi") CountryDataDAO countryDataDAO) {
        this.countryDataDAO = countryDataDAO;
    }

    /**
     * Este metodo se encagar de obtener la info del pais con la capa DAO
     * Se agrega una validacion para no consultar el DAO con un valor nulo
     * Tambien se hace la transformacion de la entidad de respuesta del DAO al DTO de la logica de negocio.
     *
     * @param countryCode el codigo de pais
     * @return el DTO que contiene la info necesaria por la logica de negocio
     */
    @Override
    @Cacheable(CacheNames.COUNTRY_DATA_CACHE)
    public CountryDataDTO getCountryDataByCountryCode(String countryCode) {
        if (countryCode == null || "".equalsIgnoreCase(countryCode)) {
            return new CountryDataDTO();
        }
        Optional<CountryData> countryData = countryDataDAO.getCountryDataByCountryCode(countryCode);
        return countryData.map(CountryDataDTO::new).orElse(new CountryDataDTO());
    }
}
