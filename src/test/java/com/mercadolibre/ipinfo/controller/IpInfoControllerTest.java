package com.mercadolibre.ipinfo.controller;

import com.mercadolibre.ipinfo.controller.request.BannedIp;
import com.mercadolibre.ipinfo.exception.ExternalServiceException;
import com.mercadolibre.ipinfo.service.IpService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class IpInfoControllerTest {

    private final String apiInfoEndpoint = "/v1/api/ip/";

    @MockBean
    private IpService ipInfoService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MongoRepository<BannedIp, String> mongoRepository;

    @Test
    public void whenValidInput_thenReturnsOk() throws Exception {
        mockMvc.perform(get(apiInfoEndpoint + "192.22.65.82")
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
    public void whenExternalServiceException_thenReturnsConflict() throws Exception {
        Mockito.when(ipInfoService.getIpInfo(Mockito.anyString()))
                .thenThrow(new ExternalServiceException("Error en la invocacion a servicios externos"));


        mockMvc.perform(get(apiInfoEndpoint + "200.200.200.200")
                .contentType("application/json"))
                .andExpect(status().isConflict());
    }
}
