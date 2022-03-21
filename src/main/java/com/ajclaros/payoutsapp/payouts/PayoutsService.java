package com.ajclaros.payoutsapp.payouts;

import com.ajclaros.payoutsapp.wakanda.WakandaService;
import org.springframework.stereotype.Service;

@Service
public class PayoutsService {

    private final WakandaService wakandaService;

    public PayoutsService(WakandaService wakandaService) {
        this.wakandaService = wakandaService;
    }

    public void executePayouts() {
        wakandaService.executePayouts();
    }
}
