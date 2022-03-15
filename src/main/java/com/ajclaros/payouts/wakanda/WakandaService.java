package com.ajclaros.payouts.wakanda;

import com.ajclaros.payouts.client.payout.PayoutClient;
import com.ajclaros.payouts.client.payout.PayoutDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class WakandaService {

    private final PayoutClient payoutClient;

    public WakandaService(PayoutClient payoutClient) {
        this.payoutClient = payoutClient;
    }

    @Scheduled(cron = "${wakanda.cron.expression}", zone = "${wakanda.cron.zone}")
    public void payout() {
        log.info("Executing payouts for Wakanda...");

        log.debug("Sending the payout to debt collection system for processing.");
        payoutClient.postPayout(new PayoutDto("asdf", new Date(), 234.324));
        log.info("Execution completed!");
    }

}
