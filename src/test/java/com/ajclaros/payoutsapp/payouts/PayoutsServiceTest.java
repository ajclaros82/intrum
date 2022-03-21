package com.ajclaros.payoutsapp.payouts;

import com.ajclaros.payoutsapp.wakanda.WakandaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class PayoutsServiceTest {

    @Mock
    private WakandaService wakandaService;

    private PayoutsService payoutsService;

    @BeforeEach
    public void setUp() {
        payoutsService = new PayoutsService(wakandaService);
    }

    @Test
    void whenCallingExecutePayoutsShouldDelegateInService() {
        payoutsService.executePayouts();

        verify(wakandaService, times(1)).executePayouts();
    }

}
