package com.sesame.test.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Sesame Api Client
 * Contains methods responsible for calling Sesame Api
 */
@Component
@RequiredArgsConstructor
public class SesameClient {

    private final RestTemplate restTemplate;

    @Value("${sesameApi.url}")
    private String SESAME_API_URL;

    /**
     * Fetches all appointments from Sesame Api
     * @return List of appointments
     */
    public List<AppointmentApi> getAppointments() {
        ResponseEntity<List<AppointmentApi>> responseEntity =
                restTemplate.exchange(SESAME_API_URL,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
       return responseEntity.getBody();
    }
}
