package com.mercadolibre.ipinfo.dao.impl;

import com.mercadolibre.ipinfo.dao.CurrencyDataDAO;
import com.mercadolibre.ipinfo.exception.ExternalServiceException;
import com.mercadolibre.ipinfo.model.fixerService.CurrencyData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;

/**
 * Esta clase implementa la interfaz CurrencyDataDAO, que define los distintos metodos que deben implementarse
 * Esta implementacion especifica devuelve informacion de una moneda especifica
 * obteniendola de una api externa a traves de llamadas http
 */
@Repository
@Qualifier("fixerApiService")
public class CurrencyDataDAOFixerImpl implements CurrencyDataDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyDataDAOFixerImpl.class);

    private static final String ACCESS_KEY_PARAM = "access_key";
    private static final String SYMBOLS_PARAM = "symbols";
    private final RestTemplate restTemplate;

    @Value("${fixer.service.endpoint}")
    private String fixerServiceEndpoint;

    @Value("${fixer.service.accesskey}")
    private String fixerServiceKey;

    public CurrencyDataDAOFixerImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Metodo encargado de realizar la llamada http para obtener la info de la moneda.
     * Por logica de negocio de la api, se consulta una moneda especifica y ademas por defaul tambien se consulta
     * por la moneda USD
     *
     * @param currencyCode codigo de moneda
     * @return un objeto opcional que puede tener o no la info solicitada
     */
    @Override
    public Optional<CurrencyData> getCurrencyDataByCurrencyCode(String currencyCode) {
        String fixerApiUrl = buildEnpointUrlWithAditionalsCodes(currencyCode, "USD");
        CurrencyData currencyData = null;
        try {
            currencyData = restTemplate.getForObject(fixerApiUrl, CurrencyData.class);
        } catch (HttpClientErrorException httpClientErrorException) {
            LOGGER.error("Hubo un error al consumir la api externa de Fixer", httpClientErrorException);
            throw new ExternalServiceException("Hubo un error al consumir la api externa de Fixer");
        }
        return Optional.ofNullable(currencyData);
    }

    /**
     * @param currencyCode codigo de moneda
     * @return El string que representa la URI que se debe invocar
     */
    private String buildEnpointUrlWithAditionalsCodes(String... currencyCode) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(fixerServiceEndpoint)
                .queryParam(ACCESS_KEY_PARAM, fixerServiceKey)
                .queryParam(SYMBOLS_PARAM, String.join(",", currencyCode));
        return builder.toUriString();
    }
}
