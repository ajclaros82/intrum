package com.ajclaros.payoutsapp.client.payout;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@ExtendWith(MockitoExtension.class)
public class PayoutClientTest {
    private static final Random RANDOM = new Random();

    private static final String PAYOUT_URI = RandomStringUtils.randomAlphabetic(10);
    private static final String COMPANY_ID_NUMBER = RandomStringUtils.randomAlphabetic(10);
    private static final String PAYMENT_DATE = RandomStringUtils.randomAlphabetic(10);
    private static final double PAYMENT_AMOUNT = RANDOM.nextDouble();

    @Mock
    private RestTemplate restTemplate;
    @Captor
    private ArgumentCaptor<String> captorUri;
    @Captor
    private ArgumentCaptor<HttpEntity<PayoutDto>> captorHttpEntity;

    private PayoutClient payoutClient;

    @BeforeEach
    public void setUp() {
        payoutClient = new PayoutClient(restTemplate, PAYOUT_URI);
    }

    @Test
    void whenCallingPostPayoutShouldDelegateInRestTemplate() {
        payoutClient.postPayout(createPayoutDto());

        verify(restTemplate, times(1)).postForEntity(captorUri.capture(), captorHttpEntity.capture(), any());

        assertEquals(PAYOUT_URI, captorUri.getValue());
        HttpEntity<PayoutDto> httpEntity = captorHttpEntity.getValue();
        HttpHeaders headers = httpEntity.getHeaders();
        assertNotNull(headers);
        assertEquals(APPLICATION_JSON, headers.getContentType());
        PayoutDto payoutDto = httpEntity.getBody();
        assertNotNull(payoutDto);
        assertEquals(COMPANY_ID_NUMBER, payoutDto.getCompanyIdentityNumber());
        assertEquals(PAYMENT_DATE, payoutDto.getPaymentDate());
        assertEquals(PAYMENT_AMOUNT, payoutDto.getPaymentAmount());
    }

    private PayoutDto createPayoutDto() {
        return PayoutDto.builder()
                .companyIdentityNumber(COMPANY_ID_NUMBER)
                .paymentDate(PAYMENT_DATE)
                .paymentAmount(PAYMENT_AMOUNT)
                .build();
    }

}
