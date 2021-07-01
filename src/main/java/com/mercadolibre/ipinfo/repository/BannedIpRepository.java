package com.mercadolibre.ipinfo.repository;

import com.mercadolibre.ipinfo.controller.request.BannedIp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BannedIpRepository extends MongoRepository<BannedIp, String> {
    Optional<BannedIp> findByIpAddress(String ipAddress);
}
