package com.mercadolibre.ipinfo.model.restCountriesService;

import lombok.Data;

import java.io.Serializable;

@Data
public class Currency implements Serializable {
    public String code;
    public String name;

    @Override
    public String toString() {
        return "Currency{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
