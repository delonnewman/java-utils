package edu.cnm.util;

import java.util.*;
import java.util.stream.Collectors;

public class Utils {
    // Set Operations
    public static Set toSet(Collection c) {
        Set s = new HashSet();
        s.addAll(c);
        return s;
    }

    public static Set difference(Collection a, Collection b) {
        Set c = new HashSet();
        c.addAll(a);
        c.removeAll(b);
        return c;
    }

    public static Set union(Collection a, Collection b) {
        Set c = new HashSet();
        c.addAll(a);
        c.addAll(b);
        return c;
    }

    public static Set intersection(Collection a, Collection b) {
        Set c = new HashSet();
        c.addAll(a);
        c.retainAll(b);
        return c;
    }

    // Relational operations
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

    // a relational projection
    public static List<Map<String, Object>> selectFields(List<Map<String, Object>> records, List<String> keys) {
        Set<String> keys_ = new TreeSet<>();
        keys_.addAll(keys);
        return records.stream()
                .map(record -> {
                    Map<String, Object> proj = new TreeMap<>();
                    for (Map.Entry<String, Object> entry: record.entrySet()) {
                        String key = entry.getKey();
                        Object val = entry.getValue();
                        if (keys_.contains(key)) {
                            proj.put(key, val);
                        }
                    }
                    return proj;
                })
                .collect(Collectors.toList());
    }

    // a negated relational projection
    public static List<Map<String, Object>> rejectFields(List<Map<String, Object>> records, List<String> keys) {
        Set<String> keys_ = new TreeSet<>();
        keys_.addAll(keys);
        return records.stream()
                .map(record -> {
                    Map<String, Object> proj = new TreeMap<>();
                    for (Map.Entry<String, Object> entry: record.entrySet()) {
                        String key = entry.getKey();
                        Object val = entry.getValue();
                        if (!keys_.contains(key)) {
                            proj.put(key, val);
                        }
                    }
                    return proj;
                })
                .collect(Collectors.toList());
    }
}