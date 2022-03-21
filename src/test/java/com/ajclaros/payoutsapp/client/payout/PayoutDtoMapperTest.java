package com.ajclaros.payoutsapp.client.payout;

import com.ajclaros.payoutsapp.wakanda.WakandaPayout;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDate;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PayoutDtoMapperTest {

    private final PayoutDtoMapper mapper = new PayoutDtoMapper();

    @Test
    void givenANullWakandaPayoutWhenMappingShouldThrowAnException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mapper.map(null));

        assertEquals("Input should not be null.", exception.getMessage());
    }

    @ParameterizedTest
    @CsvSource(value = {
            "null, null, 0",
            "companyTaxNumber, 2022-03-19, 123.123"},
            nullValues = "null")
    void givenSomeValidWakandaPayoutsWhenMappingShouldCreateAppropriatePayoutDto(String companyTaxNumber, LocalDate paymentDate, Double amount) {
        PayoutDto payoutDto = mapper.map(buildWakandaPayout(companyTaxNumber, paymentDate, amount));

        assertNotNull(payoutDto);
        assertEquals(companyTaxNumber, payoutDto.getCompanyIdentityNumber());
        assertEquals(ofNullable(paymentDate).map(LocalDate::toString).orElse(null), payoutDto.getPaymentDate());
        assertEquals(amount, payoutDto.getPaymentAmount());
    }

    private WakandaPayout buildWakandaPayout(String companyTaxNumber, LocalDate paymentDate, Double amount) {
        return WakandaPayout.builder()
                .companyTaxNumber(companyTaxNumber)
                .paymentDate(paymentDate)
                .amount(amount)
                .build();
    }

}
