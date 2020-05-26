package edu.cnm.util;

import java.text.SimpleDateFormat;
import java.util.*;

// TODO: add a parser, look for code to steal
public class JSONUtils {
    public static final SimpleDateFormat DEFAULT_DATE_FORMAT;
    static {
        // ISO 8601, JavaScript's "JSON.stringify" format
        DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    private static final String NULL  = "null";
    private static final String TRUE  = "true";
    private static final String FALSE = "false";
    private static final String OPEN_SQUARE_BRACKET = "[";
    private static final String CLOSE_SQUARE_BRACKET = "]";
    private static final String OPEN_CURLY_BRACKET = "{";
    private static final String CLOSE_CURLY_BRACKET = "}";
    private static final String COLON = ":";
    private static final String COMMA = ",";
    private static final String QUOTE = "\"";

    public static String stringify(Object value) {
        return value == null ? NULL : QUOTE + value.toString() + QUOTE;
    }

    public static String stringify(Boolean value) {
        if (value == null) return NULL;
        return value ? TRUE : FALSE;
    }

    public static String stringify(String value) {
        return value == null ? NULL : QUOTE + value + QUOTE;
    }

    public static String stringify(Number value) {
        return value == null ? NULL : value.toString();
    }

    public static String stringify(Date value, SimpleDateFormat format) {
        return value == null ? NULL : QUOTE + format.format(value) + QUOTE;
    }

    public static String stringify(Date value) {
        return stringify(value, DEFAULT_DATE_FORMAT);
    }

    // FIXME: there's got to be a better way
    public static String stringify(Set data) {
        StringBuilder sb = new StringBuilder(OPEN_SQUARE_BRACKET);
        int size = data.size();
        int i    = 1;
        for (Object val: data) {
            sb.append(_eval(val));
            if (i < size) sb.append(COMMA);
            i++;
        }
        sb.append(CLOSE_SQUARE_BRACKET);
        return sb.toString();
    }

    public static String stringify(List data) {
        StringBuilder sb = new StringBuilder(OPEN_SQUARE_BRACKET);
        int size = data.size();
        int i    = 1;
        for (Object val: data) {
            sb.append(_eval(val));
            if (i < size) sb.append(COMMA);
            i++;
        }
        sb.append(CLOSE_SQUARE_BRACKET);
        return sb.toString();
    }

    public static String stringify(Map data) {
        StringBuilder sb = new StringBuilder(OPEN_CURLY_BRACKET);
        int size = data.size();
        int i    = 1;
        for (Object entry: data.entrySet()) {
            Map.Entry entry_ = (Map.Entry)entry;
            sb.append(QUOTE);
            Object key = entry_.getKey();
            sb.append(key == null ? "" : key.toString());
            sb.append(QUOTE);
            sb.append(COLON);
            sb.append(_eval(entry_.getValue()));
            if (i < size) sb.append(COMMA);
            i++;
        }
        sb.append(CLOSE_CURLY_BRACKET);
        return sb.toString();
    }

    private static String _eval(Object val) {
        if (val == null) return NULL;
        else if (val instanceof Map) {
            Map m = (Map)val;
            return stringify(m);
        }
        else if (val instanceof List) {
            List l = (List)val;
            return stringify(l);
        }
        else if (val instanceof Collection) {
            Collection c = (Collection)val;
            return stringify(c);
        }
        else if (val instanceof Date) {
            Date d = (Date)val;
            return stringify(d);
        }
        else if (val instanceof Boolean) {
            Boolean b = (Boolean)val;
            return stringify(b);
        }
        else if (val instanceof String) {
            String s = (String)val;
            return stringify(s);
        }
        else if (val instanceof Number) {
            Number n = (Number) val;
            return stringify(n);
        }
        else {
            return stringify(val);
        }
    }
}
