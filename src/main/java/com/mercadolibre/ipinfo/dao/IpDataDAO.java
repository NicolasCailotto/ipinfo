package com.mercadolibre.ipinfo.dao;

import com.mercadolibre.ipinfo.model.ipApiService.IpData;

import java.util.Optional;

public interface IpDataDAO {

    Optional<IpData> getIpData(String ip);
}
