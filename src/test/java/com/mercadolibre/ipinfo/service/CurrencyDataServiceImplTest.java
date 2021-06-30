package com.mercadolibre.ipinfo.service;

import com.mercadolibre.ipinfo.dao.CurrencyDataDAO;
import com.mercadolibre.ipinfo.dto.CurrencyDTO;
import com.mercadolibre.ipinfo.dto.CurrencyDataDTO;
import com.mercadolibre.ipinfo.model.fixerService.CurrencyData;
import com.mercadolibre.ipinfo.service.impl.CurrencyDataServiceImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class CurrencyDataServiceImplTest {

    @Mock
    private CurrencyDataDAO currencyDataDAO;

    @Autowired
    @InjectMocks
    private CurrencyDataServiceImpl currencyDataService;

    @Test
    public void testReturnCurrencyDataWithEmptyCurrencyCode() {
        CurrencyDataDTO dto = currencyDataService.getCurrencyDataByCurrencyCode("");

        Assert.assertNull(dto.getCode());
        Assert.assertNotNull(dto.getRates());
    }

    @Test
    public void testReturnCurrencyDataWithNullCurrencyCode() {
        CurrencyDataDTO dto = currencyDataService.getCurrencyDataByCurrencyCode(null);

        Assert.assertNull(dto.getCode());
        Assert.assertNotNull(dto.getRates());
    }


    @Test
    public void test_ReturnCurrencyDataDTOTransformed_OnDAOSuccess() {
        CurrencyData currencyData = new CurrencyData();
        currencyData.setBase("EUR");

        HashMap<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", BigDecimal.valueOf(2L));
        rates.put("ARS", BigDecimal.valueOf(110L));
        currencyData.setRates(rates);

        Mockito.when(currencyDataDAO.getCurrencyDataByCurrencyCode(Mockito.anyString()))
                .thenReturn(Optional.of(currencyData));

        CurrencyDataDTO dto = currencyDataService.getCurrencyDataByCurrencyCode("ARS");

        Assert.assertEquals("ARS", dto.getCode());
        Assert.assertTrue(dto.getRates().containsKey("EUR"));
        Assert.assertTrue(dto.getRates().containsKey("USD"));
        Assert.assertEquals(BigDecimal.valueOf(0.00909), dto.getRates().get("EUR"));
        Assert.assertEquals(BigDecimal.valueOf(0.01818), dto.getRates().get("USD"));
    }

    @Test
    public void test_ReturnCurrencyDataDTOList_OnApiCallSuccess() {
        CurrencyData currencyData = new CurrencyData();
        currencyData.setBase("EUR");

        HashMap<String, BigDecimal> rates = new HashMap<>();
        rates.put("USD", BigDecimal.valueOf(2L));
        rates.put("ARS", BigDecimal.valueOf(110L));
        currencyData.setRates(rates);

        currencyData = new CurrencyData();
        currencyData.setBase("EUR");

        rates = new HashMap<>();
        rates.put("USD", BigDecimal.valueOf(2L));
        rates.put("ARS", BigDecimal.valueOf(110L));
        currencyData.setRates(rates);

        Mockito.when(currencyDataDAO.getCurrencyDataByCurrencyCode(Mockito.anyString()))
                .thenReturn(Optional.of(currencyData));

        CurrencyDTO currencyDTO = new CurrencyDTO("ARS", "Argentina Peso");
        CurrencyDTO otherCurrencyDTO = new CurrencyDTO("ARS", "Argentina Peso");
        List<CurrencyDataDTO> dtoList = currencyDataService.getCurrencyDataByCurrenciesCodes(Arrays.asList(currencyDTO, otherCurrencyDTO));

        dtoList.forEach(currencyDataDTO -> {
            Assert.assertEquals("ARS", currencyDataDTO.getCode());
            Assert.assertTrue(currencyDataDTO.getRates().containsKey("EUR"));
            Assert.assertTrue(currencyDataDTO.getRates().containsKey("USD"));
            Assert.assertEquals(BigDecimal.valueOf(0.00909), currencyDataDTO.getRates().get("EUR"));
            Assert.assertEquals(BigDecimal.valueOf(0.01818), currencyDataDTO.getRates().get("USD"));
        });
    }
}