package com.ajclaros.payoutsapp.client.payout;

import com.ajclaros.payoutsapp.wakanda.WakandaPayout;
import org.springframework.stereotype.Component;

@Component
public class PayoutDtoMapper {

    public PayoutDto map(WakandaPayout payout) {
        return PayoutDto.builder()
                .companyIdentityNumber(payout.getCompanyTaxNumber())
                .paymentAmount(payout.getAmount())
                .paymentDate(payout.getPaymentDate().toString())
                .build();
    }

}
