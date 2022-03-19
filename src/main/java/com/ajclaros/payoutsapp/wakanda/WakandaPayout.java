package com.ajclaros.payoutsapp.wakanda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@Builder
@AllArgsConstructor
public class WakandaPayout {
    String companyName;
    String companyTaxNumber;
    WakandaPayoutStatusEnum status;
    LocalDate paymentDate;
    double amount;
}
