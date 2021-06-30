package com.mercadolibre.ipinfo.dao.impl;

import com.mercadolibre.ipinfo.dao.CountryDataDAO;
import com.mercadolibre.ipinfo.exception.ExternalServiceException;
import com.mercadolibre.ipinfo.model.restCountriesService.CountryData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Esta clase implementa la interfaz CountryDataDAO, que define los distintos metodos que deben implementarse
 * Esta implementacion especifica devuelve informacion de un pais obteniendola de una api externa a traves de
 * llamadas http
 */
@Repository
@Qualifier("restCountriesApi")
public class CountryDataDAORestCountriesImpl implements CountryDataDAO {

    private final RestTemplate restTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(CountryDataDAORestCountriesImpl.class);

    @Value("${restcountry.service.endpoint:lñkjñlk}")
    private String restCountriesServiceEndpoint;

    @Autowired
    public CountryDataDAORestCountriesImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Este metodo devuelve la informacion del pais a partir de un codigo de pais
     *
     * @param countryCode codigo del pais
     * @return un objeto opcional que puede tener la informacion del pais
     */
    @Override
    public Optional<CountryData> getCountryDataByCountryCode(String countryCode) {
        // PathParam especifico del la api que se desea consumir
        String countryCodePathParam = "/{countryCode}";

        // Formo el mapa de path params para construir la url
        HashMap<String, Object> pathParams = new HashMap<>();
        pathParams.put("countryCode", countryCode);

        CountryData countryData = null;
        try {
            countryData = restTemplate.getForObject(buildUrl(restCountriesServiceEndpoint + countryCodePathParam, pathParams), CountryData.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            LOGGER.error("Hubo un error al consumir la api externa de RestCountries", httpClientErrorException);
            throw new ExternalServiceException("Hubo un error al consumir la api externa de RestCountries");
        }
        return Optional.ofNullable(countryData);
    }

    /**
     * Metodo que se encarga de hacer la transformacion de un string a una URI
     *
     * @param url        url de la api
     * @param pathParams mapa que contiene los valores de cada path param existentes en la url
     * @return la url a la que se debe hacer la llamada http
     */
    private String buildUrl(String url, Map<String, Object> pathParams) {
        return UriComponentsBuilder.fromHttpUrl(url)
                .buildAndExpand(pathParams)
                .toUriString();
    }
}
