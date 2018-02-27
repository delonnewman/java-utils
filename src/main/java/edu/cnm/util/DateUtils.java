package edu.cnm.util;

import java.text.DateFormat;
import java.time.format.DateTimeFormatter;

public class DateUtils {
    /**
     * fmtDate provides a unified interface for formatting date objects of various kinds
     * @param fmt
     * @param d
     * @return String
     */
    public static String fmtDate(DateFormat fmt, java.util.Date d) {
        return fmt.format(d);
    }

    public static String fmtDate(DateTimeFormatter fmt, java.time.LocalDate d) {
        return fmt.format(d);
    }

    public static String fmtDate(DateFormat fmt, java.sql.Date d) {
        return fmt.format(d);
    }
}
