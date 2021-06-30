package com.mercadolibre.ipinfo.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mercadolibre.ipinfo.model.ipApiService.IpData;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class IpDataDTO implements Serializable {

    public String ip;

    @JsonProperty("country_code")
    public String countryCode;

    @JsonProperty("country_name")
    public String countryName;

    public IpDataDTO(String ip) {
        this.ip = ip;
    }

    public IpDataDTO(IpData ipData) {
        this.ip = ipData.getIp();
        this.countryCode = ipData.getCountryCode();
        this.countryName = ipData.getCountryName();
    }

    @Override
    public String toString() {
        return "IpDataDTO{" +
                "ip='" + ip + '\'' +
                ", countryCode='" + countryCode + '\'' +
                ", countryName='" + countryName + '\'' +
                '}';
    }
}
