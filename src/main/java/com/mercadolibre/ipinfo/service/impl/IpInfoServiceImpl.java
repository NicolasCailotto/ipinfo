package com.mercadolibre.ipinfo.service.impl;

import com.mercadolibre.ipinfo.builder.IpInformationDTOBuilder;
import com.mercadolibre.ipinfo.config.cache.CacheNames;
import com.mercadolibre.ipinfo.controller.response.IpInformationDTO;
import com.mercadolibre.ipinfo.dto.CountryDataDTO;
import com.mercadolibre.ipinfo.dto.CurrencyDataDTO;
import com.mercadolibre.ipinfo.dto.IpDataDTO;
import com.mercadolibre.ipinfo.service.CountryDataService;
import com.mercadolibre.ipinfo.service.CurrencyDataService;
import com.mercadolibre.ipinfo.service.IpDataService;
import com.mercadolibre.ipinfo.service.IpInfoService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class contains the logic to fetch info for an specific ip
 * from different services
 */
@Service
public class IpInfoServiceImpl implements IpInfoService {

    /**
     * The IpDataService
     */
    private IpDataService ipDataService;

    /**
     * The CountryDataService
     */
    private CountryDataService countryDataService;

    /**
     * The CurrencyDataService
     */
    private CurrencyDataService currencyDataService;

    public IpInfoServiceImpl(IpDataService ipDataService, CountryDataService countryDataService, CurrencyDataService currencyDataService) {
        this.ipDataService = ipDataService;
        this.countryDataService = countryDataService;
        this.currencyDataService = currencyDataService;
    }


    /**
     * Este metodo es el encargado de hacer la llamada a los distintos servicios para generar el DTO con la informacion
     * requerida por la logica de negocio.
     * Se hacen request sincronicos debido a que la llamadas a los servicios dependen de datos provenientes llamadas
     * anteriores.
     * Si esto no fuese asi, se podrian hacer request asincronicos para ejecutarlos en paralelo a traves de la api
     * CompletableFuture de java
     * <p>
     * Con la annotation @Cacheable se indica que la respuesta de este metodo se guarde en la cache para no volver
     * a ejecutar toda la logica para una misma ip
     *
     * @param ip the specific ip
     * @return the ipInformationDTO with the specific data
     */
    @Override
    @Cacheable(CacheNames.IP_INFO_CACHE)
    public IpInformationDTO getIpInfo(final String ip) {
        IpInformationDTOBuilder ipInformationDTOBuilder = new IpInformationDTOBuilder();

        // Obtaining ip data
        IpDataDTO ipData = ipDataService.getIpData(ip);

        // Constructing ipInformationDTO
        ipInformationDTOBuilder.setIpData(ipData);

        // Obtaining country data
        CountryDataDTO countryData = countryDataService.getCountryDataByCountryCode(ipData.getCountryCode());

        // A country could have more than 1 currency, need to fetch for all of them
        List<CurrencyDataDTO> currenciesData = currencyDataService.getCurrencyDataByCurrenciesCodes(countryData.getCurrencies());
        ipInformationDTOBuilder.setCurrencies(currenciesData);

        return ipInformationDTOBuilder.build();
    }

}
