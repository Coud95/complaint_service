package com.empik.complaint_service.domain;

import io.ipinfo.api.IPinfo;
import io.ipinfo.api.cache.SimpleCache;
import io.ipinfo.api.errors.RateLimitedException;
import io.ipinfo.api.model.IPResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class CountryResolver {
    private final IPinfo ipInfo;

    public CountryResolver(@Value("${complaint-service.ipinfo.token}") String ipInfoToken) {
        this.ipInfo = new IPinfo.Builder()
                .setToken(ipInfoToken)
                .setCache(new SimpleCache(Duration.ofDays(1)))
                .build();
    }

    public String getCountryByIp(String ip) {
        try {
            IPResponse response = ipInfo.lookupIP(ip);
            if (response.getCountryCode() == null) {
                return "Unknown";
            }
            return response.getCountryName();
        } catch (RateLimitedException ex) {
            return "Rate limited";
        }
    }
}
