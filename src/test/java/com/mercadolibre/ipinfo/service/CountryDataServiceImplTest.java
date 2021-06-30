package com.mercadolibre.ipinfo.service;

import com.mercadolibre.ipinfo.dao.CountryDataDAO;
import com.mercadolibre.ipinfo.dto.CountryDataDTO;
import com.mercadolibre.ipinfo.model.restCountriesService.CountryData;
import com.mercadolibre.ipinfo.model.restCountriesService.Currency;
import com.mercadolibre.ipinfo.service.impl.CountryDataServiceImpl;
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
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CountryDataServiceImplTest {

    @Mock
    private CountryDataDAO countryDataDAO;

    @Autowired
    @InjectMocks
    private CountryDataServiceImpl countryDataService;

    @Test
    public void testReturnCountryDataWithEmptyCountryCode() {
        CountryDataDTO dto = countryDataService.getCountryDataByCountryCode("");

        Assert.assertNotNull(dto.getCurrencies());
        Assert.assertTrue(dto.getCurrencies().isEmpty());
        Assert.assertNull(dto.getCountryCode());
        Assert.assertNull(dto.getName());
    }

    @Test
    public void testReturnCountryDataWithNullCountryCode() {
        CountryDataDTO dto = countryDataService.getCountryDataByCountryCode(null);

        Assert.assertNotNull(dto.getCurrencies());
        Assert.assertTrue(dto.getCurrencies().isEmpty());
        Assert.assertNull(dto.getCountryCode());
        Assert.assertNull(dto.getName());
    }


    @Test
    public void testReturnCountryDataDTOOnDAOSuccess() {
        CountryData countryData = new CountryData();
        countryData.setName("COUNTRY_NAME");
        countryData.setAlpha3Code("COD");

        Currency currency = new Currency();
        currency.setName("Peso Argentino");
        currency.setCode("ARS");
        countryData.setCurrencies(Collections.singletonList(currency));

        Mockito.when(countryDataDAO.getCountryDataByCountryCode(Mockito.anyString()))
                .thenReturn(Optional.of(countryData));

        CountryDataDTO dto = countryDataService.getCountryDataByCountryCode("COD");
        Assert.assertNotNull(dto);
        Assert.assertEquals(dto.getName(), countryData.getName());
        Assert.assertEquals(dto.getCountryCode(), countryData.getAlpha3Code());
    }

    @Test
    public void testReturnCountryDataOnApiCallSuccess() {
        CountryData countryData = new CountryData();
        countryData.setAlpha3Code("ARS");
        countryData.setCurrencies(Collections.emptyList());
        countryData.setName("Argentina");

        Mockito.when(countryDataDAO.getCountryDataByCountryCode(Mockito.anyString()))
                .thenReturn(Optional.empty());

        CountryDataDTO dto = countryDataService.getCountryDataByCountryCode("COD");

        Assert.assertNotNull(dto.getCurrencies());
        Assert.assertTrue(dto.getCurrencies().isEmpty());
        Assert.assertNull(dto.getCountryCode());
        Assert.assertNull(dto.getName());
    }
}