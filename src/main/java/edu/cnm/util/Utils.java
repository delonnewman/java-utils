package edu.cnm.util;

import java.util.*;
import java.util.function.Function;

public class Utils {
    // Set Operations
    public static boolean isSet(Object o) {
        if (o == null) return false;
        return o instanceof Set;
    }

    public static Set toSet(Collection c) {
        return new HashSet(c);
    }

    public static Set difference(Collection a, Collection b) {
        Set c = new HashSet(a);
        c.removeAll(b);
        return c;
    }

    public static Set union(Collection a, Collection b) {
        Set c = new HashSet(a);
        c.addAll(b);
        return c;
    }

    public static Set intersection(Collection a, Collection b) {
        Set c = new HashSet(a);
        c.retainAll(b);
        return c;
    }

    // Map Operations
    public static boolean isMap(Object o) {
        if (o == null) return false;
        return o instanceof Map;
    }

    public static Object get(Map m, Object k, Object alt) {
        if (m == null || m.isEmpty()) return alt;
        Object v = m.get(k);
        if (v == null) return alt;
        else {
            return v;
        }
    }

    public static Object get(Map m, Object k) {
        return get(m, k, null);
    }

    public static Object getIn(Map m, List keys, Object alt) {
        if (m == null || m.isEmpty()) return alt;
        if (keys.size() == 1) {
            return get(m, keys.get(0), alt);
        }
        else {
            Object val = m;

            for (Object key : keys) {
                if (isMap(val)) {
                    Object newVal = get((Map) val, key);
                    if (newVal == null || (isMap(newVal) && ((Map) newVal).isEmpty())) {
                        return alt;
                    } else {
                        val = newVal;
                    }
                }
            }

            return val;
        }
    }

    public static Object getIn(Map m, List keys) {
        return getIn(m, keys, null);
    }

    // Relational operations
    // TODO: add joins, inclusion semantics, and perhaps more projections

    // TODO: add a method that takes a lambda rather than a String
    public static Map<Object, List<Map<String, Object>>> groupBy(List<Map<String, Object>> records, String field) {
        Map<Object, List<Map<String, Object>>> grouped = new HashMap<>();
        for (Map<String, Object> record: records) {
            Object value = record.get(field);
            List<Map<String, Object>> groupedValue = grouped.get(value);
            if (groupedValue == null) {
                groupedValue = new ArrayList<>();
                groupedValue.add(record);
            }
            grouped.put(value, groupedValue);
        }
        return grouped;
    }

    // a relational projector
    public static Function<Map<String, Object>, Map<String, Object>> fieldSelector(Collection<String> keys) {
        Set<String> keys_ = new TreeSet<>(keys);

        return (Map<String, Object> record) -> {
            Map<String, Object> proj = new TreeMap<>();
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                if (keys_.contains(key)) {
                    proj.put(key, val);
                }

            }
            return proj;
        };
    }

    // a negated relational projector
    public static Function<Map<String, Object>, Map<String, Object>> fieldRejector(Collection<String> keys) {
        Set<String> keys_ = new TreeSet<>(keys);

        return (Map<String, Object> record) -> {
            Map<String, Object> proj = new TreeMap<>();
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                String key = entry.getKey();
                Object val = entry.getValue();
                if (!keys_.contains(key)) {
                    proj.put(key, val);
                }

            }
            return proj;
        };
    }

    // a relational projection
    public static Function<Map<String, Object>, Map<String, Object>> fieldMapper(Map<String, String> map) {
        return (Map<String, Object> record) -> {
            Map<String, Object> proj = new TreeMap<>();
            for (Map.Entry<String, Object> entry : record.entrySet()) {
                String newKey = map.get(entry.getKey());
                if (newKey == null) continue;
                else {
                    proj.put(newKey, entry.getValue());
                }
            }
            return proj;
        };
    }
}
