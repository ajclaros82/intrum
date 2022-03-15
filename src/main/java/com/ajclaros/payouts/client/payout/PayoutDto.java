package com.ajclaros.payouts.client.payout;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Date;

@Value
@Builder
@AllArgsConstructor
public class PayoutDto {
    String companyIdentityNumber;
    Date paymentDate;
    double paymentAmount;
}
