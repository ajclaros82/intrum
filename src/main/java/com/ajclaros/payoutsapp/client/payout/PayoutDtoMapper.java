package com.ajclaros.payoutsapp.client.payout;

import com.ajclaros.payoutsapp.wakanda.WakandaPayout;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;

@Component
public class PayoutDtoMapper {

    public PayoutDto map(WakandaPayout payout) {
        if (isNull(payout)) throw new IllegalArgumentException("Input should not be null.");

        return PayoutDto.builder()
                .companyIdentityNumber(payout.getCompanyTaxNumber())
                .paymentAmount(payout.getAmount())
                .paymentDate(ofNullable(payout.getPaymentDate()).map(LocalDate::toString).orElse(null))
                .build();
    }

}
