package edu.cnm.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;

public class DateUtils {
    public static final DateFormat YYYYMMDD = new SimpleDateFormat("yyyymmdd");
    public static final DateFormat YYMMDD   = new SimpleDateFormat("yymmdd");
    public static final DateFormat HHMMSS   = new SimpleDateFormat("HHmmss");
    public static final DateFormat HHMM     = new SimpleDateFormat("HHmm");

    public static final DateFormat ISO_8601 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    /**
     * Provides a unified interface for formatting date objects of various kinds
     * @param fmt
     * @param d
     * @return String
     */
    public static String format(DateFormat fmt, java.util.Date d) {
        Objects.requireNonNull(fmt, "fmt cannot be null");
        return d == null ? "" : fmt.format(d);
    }

    public static String format(DateTimeFormatter fmt, TemporalAccessor d) {
        Objects.requireNonNull(fmt, "fmt cannot be null");
        return d == null ? "" : fmt.format(d);
    }

    public static String format(DateFormat fmt, java.time.LocalDate d) {
        Objects.requireNonNull(fmt, "fmt cannot be null");
        return d == null ? "" : fmt.format(new java.util.Date(d.toEpochDay()));
    }

    public static String format(DateFormat fmt, java.time.LocalDateTime d) {
        Objects.requireNonNull(fmt, "fmt cannot be null");
        return d == null ? "" : fmt.format(new java.util.Date(d.toEpochSecond(ZoneOffset.UTC)));
    }

    public static String format(DateFormat fmt, java.time.LocalTime t, java.time.LocalDate d) {
        Objects.requireNonNull(fmt, "fmt cannot be null");
        if (d == null) d = LocalDate.now();
        return t == null ? "" : fmt.format(new java.util.Date(d.getYear(), d.getMonthValue(), d.getDayOfMonth(), t.getHour(), t.getMinute(), t.getSecond()));
    }

    public static String format(DateFormat fmt, java.time.LocalTime t) {
        Objects.requireNonNull(fmt, "fmt cannot be null");
        return t == null ? "" : format(fmt, t, LocalDate.now());
    }

    public static String format(DateFormat fmt, java.time.Instant d) {
        Objects.requireNonNull(fmt, "fmt cannot be null");
        return d == null ? "" : fmt.format(new java.util.Date(d.getEpochSecond()));
    }

    public static String format(java.util.Date d) {
        return format(ISO_8601, d);
    }

    public static String format(TemporalAccessor d) {
        return format(DateTimeFormatter.ISO_INSTANT, d);
    }
}
