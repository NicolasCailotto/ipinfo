package com.mercadolibre.ipinfo.service;

import com.mercadolibre.ipinfo.controller.request.BannedIp;
import com.mercadolibre.ipinfo.controller.response.IpInformationDTO;

public interface IpService {

    /**
     * Method to fetch info of specific ip from different services
     *
     * @param ip the specific ip
     * @return IpInformationDTO desired ip information
     */
    IpInformationDTO getIpInfo(String ip);

    BannedIp banIp(BannedIp ip);
}
