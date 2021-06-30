package com.mercadolibre.ipinfo.dao;

import com.mercadolibre.ipinfo.dao.impl.IpDataDAOIpDataImpl;
import com.mercadolibre.ipinfo.exception.ExternalServiceException;
import com.mercadolibre.ipinfo.model.ipApiService.IpData;
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

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class IpDataDAOIpDataTest {

    @Mock
    private RestTemplate restTemplate;

    @Autowired
    @InjectMocks
    private IpDataDAOIpDataImpl ipDataIpApiService;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(ipDataIpApiService, "ipApiServiceEndoint", "http://fake.data.fixer.io/api/latest");
        ReflectionTestUtils.setField(ipDataIpApiService, "ipApiServiceKey", "face_access_key");
    }

    @Test(expected = ExternalServiceException.class)
    public void testThrowExceptionOnApiCallError() {
        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any()))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        ipDataIpApiService.getIpData("AN_IP");
    }

    @Test
    public void testReturnCountryDataOnApiCallSuccess() {
        IpData ipData = new IpData();
        ipData.setCountryCode("COUNTRY_CODE");
        ipData.setCountryName("COUNYT_NAME");
        ipData.setIp("IP");

        Mockito.when(restTemplate.getForObject(Mockito.anyString(), Mockito.any()))
                .thenReturn(ipData);

        Optional<IpData> countryOpt = ipDataIpApiService.getIpData("IP");

        Assert.assertTrue(countryOpt.isPresent());
    }
}