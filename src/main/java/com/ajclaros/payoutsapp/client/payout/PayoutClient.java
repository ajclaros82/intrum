package com.ajclaros.payoutsapp.client.payout;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@Component
public class PayoutClient {

    private final RestTemplate restTemplate;
    private final String payoutUri;

    public PayoutClient(RestTemplate restTemplate, @Value("${client.payout.uri}") String payoutUri) {
        this.restTemplate = restTemplate;
        this.payoutUri = payoutUri;
    }

    public ResponseEntity<String> postPayout(PayoutDto payoutDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(APPLICATION_JSON);
        HttpEntity<PayoutDto> payoutRequest = new HttpEntity<>(payoutDto, headers);
        return restTemplate.postForEntity(payoutUri, payoutRequest, String.class);
    }

}
