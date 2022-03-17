package com.ajclaros.payoutsapp.client.payout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class PayoutDto {
    String companyIdentityNumber;
    String paymentDate;
    double paymentAmount;
}
