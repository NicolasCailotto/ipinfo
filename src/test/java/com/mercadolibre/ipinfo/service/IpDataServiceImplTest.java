package com.mercadolibre.ipinfo.service;

import com.mercadolibre.ipinfo.dao.IpDataDAO;
import com.mercadolibre.ipinfo.dto.IpDataDTO;
import com.mercadolibre.ipinfo.model.ipApiService.IpData;
import com.mercadolibre.ipinfo.service.impl.IpDataServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class IpDataServiceImplTest {
    @Mock
    private IpDataDAO ipDataDAO;

    @Autowired
    @InjectMocks
    private IpDataServiceImpl ipDataService;

    @Test
    public void testReturnIpDataDTO() {
        IpData ipData = new IpData();
        ipData.setIp("AN_IP");
        ipData.setCountryName("COUNTRY_NAME");
        ipData.setCountryCode("COUNTRY_CODE");

        Mockito.when(ipDataDAO.getIpData(Mockito.anyString()))
                .thenReturn(Optional.of(ipData));

        IpDataDTO dto = ipDataService.getIpData("ANY_IP");

        Assert.assertNotNull(dto);
        Assert.assertEquals(ipData.getIp(), dto.getIp());
        Assert.assertEquals(ipData.getCountryCode(), dto.getCountryCode());
        Assert.assertEquals(ipData.getCountryName(), dto.getCountryName());
    }

}