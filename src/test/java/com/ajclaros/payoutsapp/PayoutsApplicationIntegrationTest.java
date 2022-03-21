package com.ajclaros.payoutsapp;

import com.ajclaros.payoutsapp.payouts.PayoutsController;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.client.ExpectedCount;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withStatus;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class PayoutsApplicationIntegrationTest {

    @Value("${client.payout.uri}")
    private String externalClientUri;

    @Autowired
    private PayoutsController payoutsController;
    @Autowired
    private RestTemplate restTemplate;

    private MockMvc mockMvc;

    private MockRestServiceServer mockServer;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(payoutsController).build();
        mockServer = MockRestServiceServer.createServer(restTemplate);
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource(value = {
            "OK",
            "NOT_FOUND",
            "INTERNAL_SERVER_ERROR"})
    void whenCallingPayoutsEndpointTriggerCallingToTheExternalAPIForEachCall(HttpStatus status) {
        mockServer.expect(ExpectedCount.times(3), requestTo(externalClientUri))
                .andExpect(method(POST))
                .andRespond(withStatus(status));

        mockMvc.perform(MockMvcRequestBuilders.get("/payouts/test")).andDo(print());

        mockServer.verify();
    }

}
