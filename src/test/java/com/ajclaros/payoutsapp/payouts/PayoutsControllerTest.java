package com.ajclaros.payoutsapp.payouts;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class PayoutsControllerTest {

    @Mock
    private PayoutsService payoutsService;

    private PayoutsController payoutsController;

    @BeforeEach
    public void setUp() {
        payoutsController = new PayoutsController(payoutsService);
    }

    @Test
    void whenCallingTestPayoutsShouldDelegateInService() {
        payoutsController.testPayouts();

        verify(payoutsService, times(1)).executePayouts();
    }

}
