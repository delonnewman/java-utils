package edu.cnm.util;

import java.util.*;
import java.util.function.Function;

public class Utils {
    // TODO: add functional utils (including a pipeline mechanism) (see: https://swannodette.github.io/mori, https://underscore.js)

    public static <T> Function<T, T> identity() {
        return x -> x;
    }

    public static Function<Void, Object> always(Object x) {
        return (v) -> x;
    }

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

    // converts nested maps into a list of de-nested maps
    /*
    public static List flatten(Map m) {
        List maps = new ArrayList();
        if (m != null && !m.isEmpty()) {
            Map m_ = m;
            Map newMap = new HashMap();
            maps.add(newMap);
            for (Object e: m_.entrySet()) {
                Map.Entry entry = (Map.Entry) e;
                Object val = entry.getValue();
                if (val instanceof Map) {
                  m_ = (Map) val;
                }
                else {
                    newMap.put(entry.getKey(), val);
                }
            }
        }
        return maps;
    }*/

    // Relational operations
    // TODO: add joins, set operations, and perhaps more projections (see: https://en.wikipedia.org/wiki/Relational_algebra)

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

    // UUID Functions
    public static UUID uuid() {
        return UUID.randomUUID();
    }

    public static UUID uuid(String id) {
        return UUID.fromString(id);
    }

    public static UUID uuid(long most, long least) {
        return new UUID(most, least);
    }

    public static UUID nameUUID(String name) {
        return UUID.nameUUIDFromBytes(name.getBytes());
    }

    public static UUID nameUUID(byte[] name) {
        return UUID.nameUUIDFromBytes(name);
    }
}
