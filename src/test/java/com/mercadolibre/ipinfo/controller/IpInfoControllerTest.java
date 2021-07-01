package com.mercadolibre.ipinfo.controller;

import com.mercadolibre.ipinfo.controller.request.BannedIp;
import com.mercadolibre.ipinfo.dao.CountryDataDAO;
import com.mercadolibre.ipinfo.dao.CurrencyDataDAO;
import com.mercadolibre.ipinfo.dao.IpDataDAO;
import com.mercadolibre.ipinfo.exception.ExternalServiceException;
import com.mercadolibre.ipinfo.model.ipApiService.IpData;
import com.mercadolibre.ipinfo.model.restCountriesService.CountryData;
import com.mercadolibre.ipinfo.model.restCountriesService.Currency;
import com.mercadolibre.ipinfo.service.CountryDataService;
import com.mercadolibre.ipinfo.service.CurrencyDataService;
import com.mercadolibre.ipinfo.service.IpDataService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import redis.embedded.RedisServer;

import java.util.Collections;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class IpInfoControllerTest {

    private final String apiInfoEndpoint = "/v1/api/ip/";

    @MockBean
    @Qualifier("ipApiService")
    private IpDataDAO ipDataDAO;

    @MockBean
    @Qualifier("restCountriesApi")
    private CountryDataDAO countryDataDAO;

    @MockBean
    @Qualifier("fixerApiService")
    private CurrencyDataDAO currencyDataDAO;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MongoRepository<BannedIp, String> mongoRepository;

    private RedisServer redisServer;

    @Before
    public void initRedisServer() {
        redisServer = RedisServer.builder().port(8082).setting("bind localhost").build();
        redisServer.start();
    }

    @After
    public void destroy() {
        if(redisServer != null && redisServer.isActive()) {
            redisServer.stop();
        }
    }

    @Test
    public void whenValidInput_thenReturnsOk() throws Exception {
        mockMvc.perform(get(apiInfoEndpoint + "192.22.65.120")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

    @Test
    public void whenBlackListedIP_thenReturnsForbibben() throws Exception {
        final String bannedIp = "192.22.65.81";
        mongoRepository.insert(new BannedIp(bannedIp));
        mockMvc.perform(get(apiInfoEndpoint + bannedIp)
                .contentType("application/json"))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenInvalidIP_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get(apiInfoEndpoint + "300.300.300.300")
                .contentType("application/json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void whenExternalServiceException_FromIpDataDAO_thenReturnsConflict() throws Exception {
        Mockito.when(ipDataDAO.getIpData(Mockito.anyString()))
                .thenThrow(new ExternalServiceException("Error en la invocacion a servicios externos"));

        mockMvc.perform(get(apiInfoEndpoint + "172.0.0.1")
                .contentType("application/json"))
                .andExpect(status().isConflict());
    }

    @Test
    public void whenExternalServiceException_FromCountryDataDAO_thenReturnsConflict() throws Exception {
        String requestIp = "200.200.200.200";
        IpData ipData = new IpData();
        ipData.setCountryCode("AR");
        ipData.setIp(requestIp);
        ipData.setCountryName("Argentina");

        Mockito.when(ipDataDAO.getIpData(Mockito.anyString()))
                .thenReturn(Optional.of(ipData));

        Mockito.when(countryDataDAO.getCountryDataByCountryCode(Mockito.anyString()))
                .thenThrow(new ExternalServiceException("Error en la invocacion a servicios externos"));

        mockMvc.perform(get(apiInfoEndpoint + requestIp)
                .contentType("application/json"))
                .andExpect(status().isConflict());
    }

    @Test
    public void whenExternalServiceException_FromCurrencyDataDAO_thenReturnsConflict() throws Exception {
        IpData ipData = new IpData();
        ipData.setCountryCode("AR");
        ipData.setIp("AN_IP");
        ipData.setCountryName("Argentina");

        Mockito.when(ipDataDAO.getIpData(Mockito.anyString()))
                .thenReturn(Optional.of(ipData));

        CountryData countryData = new CountryData();
        countryData.setAlpha3Code("ARG");
        countryData.setName("Argentina");

        Currency currency = new Currency();
        currency.setCode("AR");
        countryData.setCurrencies(Collections.singletonList(currency));

        Mockito.when(countryDataDAO.getCountryDataByCountryCode(Mockito.anyString()))
                .thenReturn(Optional.of(countryData));

        Mockito.when(currencyDataDAO.getCurrencyDataByCurrencyCode(Mockito.anyString()))
                .thenThrow(new ExternalServiceException("Error en la invocacion a servicios externos"));

        mockMvc.perform(get(apiInfoEndpoint + "172.0.0.1")
                .contentType("application/json"))
                .andExpect(status().isConflict());
    }
}
