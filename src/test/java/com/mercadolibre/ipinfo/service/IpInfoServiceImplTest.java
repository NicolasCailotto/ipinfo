package com.mercadolibre.ipinfo.service;

import com.mercadolibre.ipinfo.controller.response.IpInformationDTO;
import com.mercadolibre.ipinfo.dto.CountryDataDTO;
import com.mercadolibre.ipinfo.dto.CurrencyDTO;
import com.mercadolibre.ipinfo.dto.CurrencyDataDTO;
import com.mercadolibre.ipinfo.dto.IpDataDTO;
import com.mercadolibre.ipinfo.service.impl.IpServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class IpInfoServiceImplTest {

    @Mock
    private IpDataService ipDataService;

    @Mock
    private CountryDataService countryDataService;

    @Mock
    private CurrencyDataService currencyDataService;

    @Autowired
    @InjectMocks
    private IpServiceImpl ipInfoService;

    @Test
    public void testReturnIpInformationDTO() {
        IpDataDTO ipData = new IpDataDTO();
        ipData.setIp("AN_IP");
        ipData.setCountryName("COUNTRY_NAME");
        ipData.setCountryCode("COUNTRY_CODE");

        CountryDataDTO countryDataDTO = new CountryDataDTO();
        countryDataDTO.setCountryCode("AR");
        countryDataDTO.setName("Argentina");
        CurrencyDTO currencyDTO = new CurrencyDTO();
        currencyDTO.setCode("ARS");
        currencyDTO.setName("Peso Argentino");
        countryDataDTO.setCurrencies(Collections.singletonList(currencyDTO));

        CurrencyDataDTO currencyDataDTO = new CurrencyDataDTO();
        countryDataDTO.setName("ARS");
        currencyDataDTO.setCode("Peso Argentino");

        Mockito.when(ipDataService.getIpData(Mockito.anyString()))
                .thenReturn(ipData);

        Mockito.when(countryDataService.getCountryDataByCountryCode(Mockito.anyString()))
                .thenReturn(countryDataDTO);
        Mockito.when(currencyDataService.getCurrencyDataByCurrenciesCodes(Mockito.anyList()))
                .thenReturn(Collections.singletonList(currencyDataDTO));

        IpInformationDTO dto = ipInfoService.getIpInfo("ANY_IP");

        Assert.assertNotNull(dto);
        Assert.assertEquals(ipData.getIp(), dto.getIp());
        Assert.assertEquals(ipData.getCountryCode(), dto.getCountryCode());
        Assert.assertEquals(ipData.getCountryName(), dto.getCountryName());
        Assert.assertTrue(!dto.getCurrencies().isEmpty());

        Assert.assertEquals(ipData.getCountryCode(), dto.getCountryCode());
        Assert.assertEquals(ipData.getCountryName(), dto.getCountryName());
    }

}