package com.ajclaros.payoutsapp.wakanda;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;

import static com.ajclaros.payoutsapp.wakanda.WakandaPayoutStatusEnum.PENDING;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WakandaPayoutMapperTest {

    WakandaPayoutMapper mapper = new WakandaPayoutMapper();

    @Test
    void givenANullInputStringWhenMappingShouldThrowAnException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mapper.map(null));

        assertEquals("Input should not be null.", exception.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    @CsvSource(value = {"this line will not match with the wakanda pattern"})
    void givenAnInvalidInputStringWhenMappingShouldThrowAnException(String wakandaInvalidLine) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> mapper.map(wakandaInvalidLine));

        assertEquals("Line does not match the expected pattern.", exception.getMessage());
    }

    @Test
    @SneakyThrows
    void givenSomeValidWakandaPayoutsWhenMappingShouldCreateAppropriatePayoutDto() {
        String wakandaValidLine = "\"Iron suites\";\"156-5562415\";\"PENDING\";\"2023-11-17\";\"7000,10\";";

        WakandaPayout wakandaPayout = mapper.map(wakandaValidLine);

        assertNotNull(wakandaPayout);
        assertEquals("Iron suites", wakandaPayout.getCompanyName());
        assertEquals("156-5562415", wakandaPayout.getCompanyTaxNumber());
        assertEquals(PENDING, wakandaPayout.getStatus());
        assertEquals("2023-11-17", wakandaPayout.getPaymentDate().toString());
        assertEquals(7000.10, wakandaPayout.getAmount());
    }

}
