package com.mercadolibre.ipinfo.service.impl;

import com.mercadolibre.ipinfo.builder.IpInformationDTOBuilder;
import com.mercadolibre.ipinfo.config.cache.CacheNames;
import com.mercadolibre.ipinfo.controller.request.BannedIp;
import com.mercadolibre.ipinfo.controller.response.IpInformationDTO;
import com.mercadolibre.ipinfo.dto.CountryDataDTO;
import com.mercadolibre.ipinfo.dto.CurrencyDataDTO;
import com.mercadolibre.ipinfo.dto.IpDataDTO;
import com.mercadolibre.ipinfo.exception.BannedIpExistException;
import com.mercadolibre.ipinfo.repository.BannedIpRepository;
import com.mercadolibre.ipinfo.service.CountryDataService;
import com.mercadolibre.ipinfo.service.CurrencyDataService;
import com.mercadolibre.ipinfo.service.IpDataService;
import com.mercadolibre.ipinfo.service.IpService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * This class contains the logic to fetch info for an specific ip
 * from different services
 */
@Service
public class IpServiceImpl implements IpService {

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

    /**
     * The BannedIpRepository
     */
    private BannedIpRepository bannedIpRepository;

    public IpServiceImpl(IpDataService ipDataService, CountryDataService countryDataService, CurrencyDataService currencyDataService, BannedIpRepository bannedIpRepository) {
        this.ipDataService = ipDataService;
        this.countryDataService = countryDataService;
        this.currencyDataService = currencyDataService;
        this.bannedIpRepository = bannedIpRepository;
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

    /**
     * Metodo encargado de guardar un ip para ser baneada
     * Previamente se valida que el address ya no exista en DB
     * @param ip que se desea banear
     * @return el objeto de ip baneado
     */
    @Override
    public BannedIp banIp(BannedIp ip) {
        Optional<BannedIp> bannedIp = bannedIpRepository.findByIpAddress(ip.getIpAddress());
        bannedIp.ifPresent(existingIp -> {throw new BannedIpExistException("La IP ya se encuentra baneada.");});
        return bannedIpRepository.insert(ip);
    }

}
