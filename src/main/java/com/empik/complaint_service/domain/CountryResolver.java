package com.empik.complaint_service.domain;

import io.ipinfo.api.IPinfo;
import io.ipinfo.api.cache.SimpleCache;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CountryResolver {
    private static final Logger LOGGER = LoggerFactory.getLogger(CountryResolver.class);
    private final IPinfo ipInfo;
    private static final String UNKNOWN_COUNTRY = "Unknown";

    public CountryResolver(@Value("${complaint-service.ipinfo.token}") String ipInfoToken) {
        this.ipInfo = new IPinfo.Builder()
                .setToken(ipInfoToken)
                .setCache(new SimpleCache(Duration.ofDays(1)))
                .build();
    }

    public String getCountryByIp(String ip) {
        try {
            IPResponse response = ipInfo.lookupIP(ip);
            if (response.getCountryName() == null) {
                LOGGER.warn("Country name is null for IP: {}", ip);
                return UNKNOWN_COUNTRY;
            }
            return response.getCountryName();
        } catch (RateLimitedException ex) {
            LOGGER.error("Rate limit exceeded for IP: {}. Error: {}", ip, ex.getMessage());
            return UNKNOWN_COUNTRY;
        }
    }
}
