package edu.cnm.util;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class CSVUtils {

    public static List<List<String>> parse(String csvData) {
        String[] lines = csvData.split("\\r?\\n");
        List<List<String>> data = new ArrayList<List<String>>();
        for (String line: lines) {
            data.add(parseLine(line + "\n"));
        }
        return data;
    }

    // assumes the first row is field names
    public static List<Map<String, Object>> parseAsRecords(String csvData) throws Exception {
        String[] lines = csvData.split("\\r?\\n");
        System.out.println(lines.length);
        List<Map<String, Object>> data = new ArrayList<>();
        if (lines.length == 0 || lines.length == 1) {
            return data;
        }
        else {
            List<String> fields = parseLine(lines[0]);
            if (lines.length > Integer.MAX_VALUE) throw new Exception("Record length has exceded maximum value");
            for (int i = 1; i < lines.length; i++) {
                List<String>        row    = parseLine(lines[i]);
                Map<String, Object> record = new HashMap<>();
                System.out.println(i);
                for (int j = 0; j < fields.size(); j++) {
                    record.put(fields.get(j), row.get(j));
                }
                data.add(record);
            }
            return data;
        }
    }

    /**
     * Split a comma-separated string into individual values. Follows correct
     * RFC 4180 syntax when parsing quoted values and ignores a (single) line
     * separator at the end of the string, if present. All other whitespace is
     * preserved inside the returned strings. The way we handle the empty string
     * allows us to represent any possible list of strings as a (not necessarily
     * unique) CSV string argument: The list containing a single empty String
     * can be represented by a CSV value of "\"\"" (a string consisting of two
     * quotes), while the empty list itself is represented by "" (empty string).
     * <p>
     * The CSV specification does not provide for any specific meaning of the
     * parsed data except as sequences of characters. It's up to the application
     * to interpret the values as different data types, e.g., numbers, or dates.
     *
     * @param csvData The CSV string to be parsed.
     * @return A list of strings inside the columns, or emptyList() if the
     * argument was the empty string.
     */
    public static List<String> parseLine(String csvData) {
        Objects.requireNonNull(csvData, "CSV string may not be null.");

        int size = lineSize(csvData);

        if (size == 0) {
            return Collections.emptyList();
        }

        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        AtomicBoolean isQuoted = new AtomicBoolean();
        AtomicBoolean quoteInQuote = new AtomicBoolean();

        csvData.chars().limit(size).forEach(c -> {
            if (!isQuoted.get()) {
                if (c == '"' && sb.length() == 0) {
                    isQuoted.set(true);
                } else if (c == ',') {
                    result.add(sb.toString());
                    sb.setLength(0);
                } else {
                    sb.append((char) c);
                }
            } else if (!quoteInQuote.getAndSet(false)) {
                if (c == '"') {
                    quoteInQuote.set(true);
                } else {
                    sb.append((char) c);
                }
            } else if (c == '"') {
                sb.append((char) c);
            } else if (c == ',') {
                isQuoted.set(false);
                result.add(sb.toString());
                sb.setLength(0);
            } else {
                throw new IllegalArgumentException(
                        "Unescaped quote: {" + csvData.substring(0, size) + "}");
            }
        });

        if (isQuoted.get() && !quoteInQuote.get()) {
            throw new IllegalArgumentException(
                    "Unbalanced quote: {" + csvData.substring(0, size) + "}");
        }

        result.add(sb.toString());

        return result;
    }

    /**
     * Calculate the length of a String not including a (single) possible line
     * separator, which may be either "\r", "\n", or "\r\n".
     *
     * @param s
     * @return The length of s not including the line separator.
     */
    public static int lineSize(String s) {
        if (s.endsWith("\r\n")) {
            return s.length() - 2;
        }

        if (s.endsWith("\r") || s.endsWith("\n")) {
            return s.length() - 1;
        }

        return s.length();
    }

    /**
     * Convert a list into a comma-separated string for output to a CSV file.
     * Follows correct RFC 4180 syntax when quoting elements, and only quotes if
     * necessary.
     *
     * @param data The list to be converted to a CSV string.
     * @return The string representing the list in a CSV file (not including a
     * line separator).
     */
    public static String stringifyLine(Collection data) {
        if (data == null || (data.size() == 1 && data.toString().isEmpty())) {
            return "\"\"";
        }

        StringBuilder sb = new StringBuilder();

        int i = 0;
        for (Object item: data) {
            sb.append(item == null ? "" : quote(item.toString()));
            if (i != data.size() - 1) {
                sb.append(",");
            }
            i++;
        }

        return sb.toString() + "\n";
    }

    public static String stringify(List<List<Object>> data) {
        StringBuilder buffer = new StringBuilder();
        for (List<Object> row: data) {
            buffer.append(stringifyLine(row));
        }
        return buffer.toString();
    }

    public static String stringifyRecords(List<Map<String, Object>> records) {
        StringBuilder buffer = new StringBuilder();
        Set<String> fields   = records.get(0).keySet();
        buffer.append(stringifyLine(fields));
        for (Map<String, Object> record: records) {
            buffer.append(stringifyRecord(record));
        }
        return buffer.toString();
    }

    public static String stringifyRecord(Map<String, Object> record) {
        return stringifyLine(record.values());
    }

    /**
     * Quote an element for inclusion in a CSV file (if necessary). If no quotes
     * are required, returns the string unmodified.
     *
     * @param s The string to be quoted.
     * @return The quoted or unmodified string.
     */
    public static String quote(String s) {
        if (s != null && (s.contains("\"") || s.contains(","))) {
            return '"' + s.replace("\"", "\"\"") + '"';
        }

        return s;
    }
}
