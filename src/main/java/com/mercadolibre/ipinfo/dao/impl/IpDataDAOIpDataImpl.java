package com.mercadolibre.ipinfo.dao.impl;

import com.mercadolibre.ipinfo.dao.IpDataDAO;
import com.mercadolibre.ipinfo.exception.ExternalServiceException;
import com.mercadolibre.ipinfo.model.ipApiService.IpData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Esta clase implementa la interfaz IpDataDAO, que define los distintos metodos que deben implementarse
 * Esta implementacion especifica devuelve informacion de una direccion IP especifica
 * obteniendola de una api externa a traves de llamadas http
 */
@Repository
@Qualifier("ipApiService")
public class IpDataDAOIpDataImpl implements IpDataDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(IpDataDAOIpDataImpl.class);

    private final RestTemplate restTemplate;

    @Value("${ipapi.service.endpoint}")
    private String ipApiServiceEndoint;

    @Value("${ipapi.service.accesskey}")
    private String ipApiServiceKey;

    @Autowired
    public IpDataDAOIpDataImpl(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Este metodo devuelve la informacion de la direccion IP solicitada
     *
     * @param ip dirrecion de ip
     * @return un objeto opcional que puede tener la informacion de la ip
     */
    @Override
    public Optional<IpData> getIpData(String ip) {
        // PathParam especifico del la api que se desea consumir
        String ipAdressPathParam = "/{ipAdress}";

        // Formo el mapa de path params para construir la url
        HashMap<String, Object> pathParams = new HashMap<>();
        pathParams.put("ipAdress", ip);

        IpData ipData = null;
        try {
            ipData = restTemplate.getForObject(buildEnpointUrl(ipApiServiceEndoint + ipAdressPathParam, pathParams), IpData.class);
        } catch (Exception httpClientErrorException) {
            LOGGER.error("Hubo un error al consumir la api externa de IpApi", httpClientErrorException);
            throw new ExternalServiceException("Hubo un error al consumir la api externa de IpApi");
        }
        return Optional.ofNullable(ipData);
    }

    /**
     * @param url
     * @param pathParams
     * @return
     */
    private String buildEnpointUrl(String url, Map<String, Object> pathParams) {
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("access_key", ipApiServiceKey)
                .buildAndExpand(pathParams)
                .toUriString();
    }

}
