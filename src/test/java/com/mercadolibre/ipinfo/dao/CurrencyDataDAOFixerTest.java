package com.mercadolibre.ipinfo.dao;

import com.mercadolibre.ipinfo.dao.impl.CurrencyDataDAOFixerImpl;
import com.mercadolibre.ipinfo.exception.ExternalServiceException;
import com.mercadolibre.ipinfo.model.fixerService.CurrencyData;
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
public class CurrencyDataDAOFixerTest {

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    private CurrencyDataDAOFixerImpl currencyDataFixerService;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(currencyDataFixerService, "fixerServiceEndpoint", "http://fake.data.fixer.io/api/latest");
        ReflectionTestUtils.setField(currencyDataFixerService, "fixerServiceKey", "face_access_key");
    }

    @Test(expected = ExternalServiceException.class)
    public void testThrowExceptionOnApiCallError() {
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        currencyDataFixerService.getCurrencyDataByCurrencyCode("CURRENCY_CODE");
    }

    @Test
    public void testReturnCountryDataOnApiCallSuccess() {
        CurrencyData currencyData = new CurrencyData();
        currencyData.setBase("EUR");
        currencyData.setRates(Collections.emptyMap());

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any()))
                .thenReturn(currencyData);

        Optional<CurrencyData> currencyOpt = currencyDataFixerService.getCurrencyDataByCurrencyCode("CURRENCY_CODE");

        Assert.assertTrue(currencyOpt.isPresent());
    }
}