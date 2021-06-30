package com.mercadolibre.ipinfo.builder;

import com.mercadolibre.ipinfo.controller.response.IpInformationDTO;
import com.mercadolibre.ipinfo.dto.CurrencyDataDTO;
import com.mercadolibre.ipinfo.dto.IpDataDTO;

import java.util.List;

public class IpInformationDTOBuilder {

    private IpInformationDTO dto;

    public IpInformationDTOBuilder() {
        this.dto = new IpInformationDTO();
    }

    public IpInformationDTO setCurrencies(List<CurrencyDataDTO> currencies) {
        if (currencies != null) {
            this.dto.setCurrencies(currencies);
        }
        return this.dto;
    }

    public IpInformationDTO setIpData(IpDataDTO ipData) {
        dto.setCountryCode(ipData.getCountryCode());
        dto.setCountryName(ipData.getCountryName());
        dto.setIp(ipData.getIp());
        return this.dto;
    }

    public IpInformationDTO build() {
        return this.dto;
    }
}
