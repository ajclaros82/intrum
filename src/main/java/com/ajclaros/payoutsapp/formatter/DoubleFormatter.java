package com.ajclaros.payoutsapp.formatter;

import lombok.SneakyThrows;

import java.text.NumberFormat;
import java.util.Locale;

public class DoubleFormatter {

    @SneakyThrows
    static public double commaSeparatedFormat(String doubleStr) {
        return NumberFormat.getInstance(Locale.FRANCE).parse(doubleStr).doubleValue();
    }

}
