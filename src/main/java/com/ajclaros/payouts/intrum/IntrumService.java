package com.ajclaros.payouts.intrum;

import com.ajclaros.payouts.wakanda.WakandaService;
import org.springframework.stereotype.Service;

@Service
public class IntrumService {

    private final WakandaService wakandaService;

    public IntrumService(WakandaService wakandaService) {
        this.wakandaService = wakandaService;
    }

    public void executePayouts() {
        wakandaService.payout();
    }
}
