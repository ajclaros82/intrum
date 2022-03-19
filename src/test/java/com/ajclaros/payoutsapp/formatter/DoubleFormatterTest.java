package com.ajclaros.payoutsapp.formatter;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DoubleFormatterTest {

    @Test
    void givenNullInputWhenFormattingShouldThrowAnException() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> DoubleFormatter.commaSeparatedFormat(null));

        assertEquals("Input should not be null.", exception.getMessage());
    }

    @ParameterizedTest
    @EmptySource
    @ValueSource(strings = {"badDouble", "d123.123", "d123,123"})
    void givenBadDoubleStringsWhenFormattingShouldThrowAnException(String input) {
        assertThrows(ParseException.class, () -> DoubleFormatter.commaSeparatedFormat(input));
    }

    @SneakyThrows
    @ParameterizedTest
    @CsvSource(value = {
            "123.123 > 123.0",
            "123.123.123 > 123.0",
            "123,123 > 123.123",
            "123,123,123 > 123.123"},
            delimiter = '>')
    void givenBadDoubleStringsWhenFormattingShouldThrowAnException(String input, double expected) {
        double result = DoubleFormatter.commaSeparatedFormat(input);

        assertEquals(expected, result);
    }

}
