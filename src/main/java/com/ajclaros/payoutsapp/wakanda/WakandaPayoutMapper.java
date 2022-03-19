package com.ajclaros.payoutsapp.wakanda;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.ajclaros.payoutsapp.formatter.DoubleFormatter.commaSeparatedFormat;
import static java.util.Objects.isNull;

@Slf4j
@Component
public class WakandaPayoutMapper {
    private static final Pattern WAKANDA_PATTERN = Pattern.compile("\"([\\w\\s,.-]+)\";\"(\\d{3}-\\d{7})\";\"(PAID|PENDING)\";\"(\\d{4}-\\d{2}-\\d{2})\";\"(\\d+(?:,\\d+)?)\";");

    private static final int COMPANY_NAME_POS = 1;
    private static final int COMPANY_TAX_NUMBER_POS = 2;
    private static final int STATUS_POS = 3;
    private static final int PAYMENT_DATE_POS = 4;
    private static final int AMOUNT_POS = 5;

    public WakandaPayout map(String str) throws IllegalArgumentException, ParseException {
        if (isNull(str)) throw new IllegalArgumentException("Input should not be null.");

        Matcher matcher = WAKANDA_PATTERN.matcher(str);
        if (!matcher.matches()) throw new IllegalArgumentException("Line does not match the expected pattern.");

        return WakandaPayout.builder()
                .companyName(matcher.group(COMPANY_NAME_POS))
                .companyTaxNumber(matcher.group(COMPANY_TAX_NUMBER_POS))
                .status(WakandaPayoutStatusEnum.valueOf(matcher.group(STATUS_POS)))
                .paymentDate(LocalDate.parse(matcher.group(PAYMENT_DATE_POS)))
                .amount(commaSeparatedFormat(matcher.group(AMOUNT_POS)))
                .build();
    }

}
