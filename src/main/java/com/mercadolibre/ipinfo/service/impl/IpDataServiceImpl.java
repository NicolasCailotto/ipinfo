package com.mercadolibre.ipinfo.service.impl;

import com.mercadolibre.ipinfo.config.cache.CacheNames;
import com.mercadolibre.ipinfo.dao.IpDataDAO;
import com.mercadolibre.ipinfo.dto.IpDataDTO;
import com.mercadolibre.ipinfo.model.ipApiService.IpData;
import com.mercadolibre.ipinfo.service.IpDataService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Esta clase contiene la logica para obtener la informacion especifica de una direccion de ip
 * desde la capa de DAO
 */
@Service
public class IpDataServiceImpl implements IpDataService {

    /**
     * The IpDataDAO
     * El dao injectado en este caso es el que obtiene la info desde un api externa
     */
    private final IpDataDAO ipDataDAO;

    public IpDataServiceImpl(@Qualifier("ipApiService") IpDataDAO ipDataDAO) {
        this.ipDataDAO = ipDataDAO;
    }

    /**
     * Metodo que consulta la capa DAO para obtener la informacion de una IP especifica
     * Con la annotation @Cacheble especificamos el la respuesta de este metodo se cachee para no tener que volver a
     * consultarla reiteradas veces
     *
     * @param ip
     * @return
     */
    @Override
    @Cacheable(value = CacheNames.IP_DATA_CACHE, unless = "#result==null")
    public IpDataDTO getIpData(String ip) {
        Optional<IpData> ipData = ipDataDAO.getIpData(ip);
        return ipData.map(IpDataDTO::new).orElse(new IpDataDTO());
    }
}
