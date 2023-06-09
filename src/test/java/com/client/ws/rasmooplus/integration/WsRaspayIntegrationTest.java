package com.client.ws.rasmooplus.integration;

import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.integration.impl.WsRaspayIntegrationImpl;
import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;


@ExtendWith(MockitoExtension.class)
class WsRaspayIntegrationTest {

    private static HttpHeaders headers;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WsRaspayIntegrationImpl wsRaspayIntegration;

    @BeforeAll
    public static void loadHeaders() {
        headers = getHttpHeaders();
    }

    @Test
    void given_createCustomer_when_apiResponseIs201Create_then_returnCustomerDto() {
        ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration, "customerUrl", "/v1/customer");

        CustomerDto dto = new CustomerDto();
        dto.setId(String.valueOf(UUID.randomUUID()));
        dto.setCpf("12345678901");
        HttpEntity<CustomerDto> request = new HttpEntity<>(dto, this.headers);

        Mockito.when(restTemplate.exchange("http://localhost:8080/v1/customer",
                HttpMethod.POST,
                request,
                CustomerDto.class
        )).thenReturn(ResponseEntity.of(Optional.of(dto)));

        Assertions.assertEquals(dto,wsRaspayIntegration.createCustomer(dto));
        Mockito.verify(restTemplate, Mockito.times(1)).exchange("http://localhost:8080/v1/customer",
                HttpMethod.POST,
                request,
                CustomerDto.class
        );
    }

    @Test
    void given_createCustomer_when_apiResponseIs400BadRequest_then_returnNull() {
        ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration, "customerUrl", "/v1/customer");

        CustomerDto dto = new CustomerDto();
        dto.setId(String.valueOf(UUID.randomUUID()));
        dto.setCpf("12345678901");
        HttpEntity<CustomerDto> request = new HttpEntity<>(dto, this.headers);

        Mockito.when(restTemplate.exchange("http://localhost:8080/v1/customer",
                HttpMethod.POST,
                request,
                CustomerDto.class
        )).thenReturn(ResponseEntity.badRequest().build());

        Assertions.assertNull(wsRaspayIntegration.createCustomer(dto));

        Mockito.verify(restTemplate, Mockito.times(1)).exchange("http://localhost:8080/v1/customer",
                HttpMethod.POST,
                request,
                CustomerDto.class
        );
    }

    @Test
    void given_createCustomer_when_apiResponseGetThrows_then_throwHttpClientException() {
        ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
        ReflectionTestUtils.setField(wsRaspayIntegration, "customerUrl", "/v1/customer");

        CustomerDto dto = new CustomerDto();
        dto.setId(String.valueOf(UUID.randomUUID()));
        dto.setCpf("12345678901");
        HttpEntity<CustomerDto> request = new HttpEntity<>(dto, this.headers);

        Mockito.when(restTemplate.exchange("http://localhost:8080/v1/customer",
                HttpMethod.POST,
                request,
                CustomerDto.class
        )).thenThrow(HttpClientErrorException.class);

        Assertions.assertThrows(HttpClientErrorException.class, () -> wsRaspayIntegration.createCustomer(dto));

        Mockito.verify(restTemplate, Mockito.times(1)).exchange("http://localhost:8080/v1/customer",
                HttpMethod.POST,
                request,
                CustomerDto.class
        );
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        String credential = "rasmooplus:r@sm00";
        String base64 = new String (Base64.encodeBase64(credential.getBytes(), false));
        headers.add("Authorization","Basic "+base64);
        return headers;
    }
}
