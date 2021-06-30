package com.mercadolibre.ipinfo.dao;

import com.mercadolibre.ipinfo.dao.impl.CountryDataDAORestCountriesImpl;
import com.mercadolibre.ipinfo.exception.ExternalServiceException;
import com.mercadolibre.ipinfo.model.restCountriesService.CountryData;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CountryDataDAORestCountriesTest {

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    private CountryDataDAORestCountriesImpl countryDataRestCountriesService;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(countryDataRestCountriesService, "restCountriesServiceEndpoint", "http://fake.restcountries.eu/rest/v2/alpha");
    }

    @Test(expected = ExternalServiceException.class)
    public void testThrowExceptionOnApiCallError() {
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        countryDataRestCountriesService.getCountryDataByCountryCode("ANY_COUNTRY");
    }

    @Test
    public void testReturnCountryDataOnApiCallSuccess() {
        CountryData countryData = new CountryData();
        countryData.setAlpha3Code("ARS");
        countryData.setCurrencies(Collections.emptyList());
        countryData.setName("Argentina");

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any()))
                .thenReturn(countryData);

        Optional<CountryData> countryOpt = countryDataRestCountriesService.getCountryDataByCountryCode("ANY_COUNTRY");

        Assert.assertTrue(countryOpt.isPresent());
    }
}
