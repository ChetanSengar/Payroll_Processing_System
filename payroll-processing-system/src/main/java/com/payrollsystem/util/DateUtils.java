package com.payrollsystem.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Utility class for date conversion operations
 * Not using currently, for reference only
 */
public class DateUtils {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");

    public static Date convertToDate(final LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static Date convertStringToDate(final String dateString) {
        try {
            return DATE_FORMAT.parse(dateString);
        } catch (final ParseException e) {
            throw new RuntimeException("Error parsing date: " + dateString, e);
        }
    }

}
