package com.mercadolibre.ipinfo.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Document(value = "banned_ips")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BannedIp {

    @Id
    private String _id;

    @NotNull
    @NotBlank
    private String ipAddress;

    public BannedIp(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}
