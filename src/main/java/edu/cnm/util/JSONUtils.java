package edu.cnm.util;

import java.util.Map;
import java.util.List;

// TODO: add a parser, look for code to steal
public class JSONUtils {
    // TODO: add dates
    public static String stringify(Object value) {
        return value == null ? "null" : "\"" + value.toString() + "\"";
    }

    public static String stringify(Boolean value) {
        return value == true ? "true" : "false";
    }

    public static String stringify(String value) {
        return value == null ? "null" : "\"" + value + "\"";
    }

    public static String stringify(List<Object> data) {
        StringBuilder sb = new StringBuilder("[");
        int size = data.size();
        int i    = 1;
        for (Object val: data) {
            sb.append(stringify(val));
            if (i < size) sb.append(",");
            i++;
        }
        sb.append("]");
        return sb.toString();
    }

    public static String stringify(Map<String, Object> data) {
        StringBuilder sb = new StringBuilder("{");
        int size = data.size();
        int i    = 1;
        for (Map.Entry<String, Object> entry: data.entrySet()) {
            sb.append("\"");
            sb.append(entry.getKey());
            sb.append("\":");
            sb.append(stringify(entry.getValue()));
            if (i < size) sb.append(",");
            i++;
        }
        sb.append("}");
        return sb.toString();
    }
}
