package edu.cnm.util;

public class StringUtils {
    // repeat string n number of times
    public static String times(int n, String str) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
