package com.empik.complaint_service.domain;

import io.ipinfo.api.IPinfo;
import io.ipinfo.api.model.IPResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryResolverTest {

    private static final String VALID_IP = "127.0.0.1";
    private static final String INVALID_IP = "9999.9999.9999.9999";
    private static final String COUNTRY_NAME = "United States";
    private static final String UNKNOWN_COUNTRY = "Unknown";

    @Mock
    private IPinfo ipInfo;

    @Mock
    private IPResponse ipResponse;

    private CountryResolver countryResolver;

    @BeforeEach
    void setUp() {
        countryResolver = new CountryResolver("test_token");
        ReflectionTestUtils.setField(countryResolver, "ipInfo", ipInfo);
    }


    @Test
    void getCountryByIpReturnsCountryNameForValidIp() throws Exception {
        when(ipInfo.lookupIP(VALID_IP)).thenReturn(ipResponse);
        when(ipResponse.getCountryName()).thenReturn(COUNTRY_NAME);

        String result = countryResolver.getCountryByIp(VALID_IP);

        assertEquals(COUNTRY_NAME, result);
    }

    @Test
    void getCountryByIpReturnsUnknownForInvalidIp() throws Exception {
        when(ipInfo.lookupIP(INVALID_IP)).thenReturn(ipResponse);
        when(ipResponse.getCountryName()).thenReturn(null);

        String result = countryResolver.getCountryByIp(INVALID_IP);

        assertEquals(UNKNOWN_COUNTRY, result);
    }

}