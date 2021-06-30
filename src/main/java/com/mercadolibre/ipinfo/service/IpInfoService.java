package com.mercadolibre.ipinfo.service;

import com.mercadolibre.ipinfo.controller.response.IpInformationDTO;

public interface IpInfoService {

    /**
     * Method to fetch info of specific ip from different services
     *
     * @param ip the specific ip
     * @return IpInformationDTO desired ip information
     */
    IpInformationDTO getIpInfo(String ip);
}
