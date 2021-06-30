package com.mercadolibre.ipinfo.service;

import com.mercadolibre.ipinfo.dto.IpDataDTO;

public interface IpDataService {
    /**
     * Method to fetch info of an specific ip
     *
     * @param ip the specific ip
     * @return IpData desired ip information
     */
    IpDataDTO getIpData(String ip);
}
