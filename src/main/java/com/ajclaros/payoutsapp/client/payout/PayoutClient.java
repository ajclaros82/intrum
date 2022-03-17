package com.ajclaros.payoutsapp.client.payout;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class PayoutClient {

    private final String payoutUri;

    public PayoutClient(@Value("${client.payout.uri}") String payoutUri) {
        this.payoutUri = payoutUri;
    }

    public ResponseEntity<String> postPayout(PayoutDto payoutDto) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PayoutDto> payoutRequest = new HttpEntity<>(payoutDto, headers);
        return new RestTemplate().postForEntity(payoutUri, payoutRequest, String.class);
    }

}
