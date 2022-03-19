package com.ajclaros.payoutsapp.formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import static java.util.Objects.isNull;

public class DoubleFormatter {

    static public double commaSeparatedFormat(String doubleStr) throws ParseException {
        if (isNull(doubleStr)) throw new IllegalArgumentException("Input should not be null.");

        return NumberFormat.getInstance(Locale.FRANCE).parse(doubleStr).doubleValue();
    }

}
