package edu.cnm.util;

import java.io.*;

public class IOUtils {
    public static final int DEFAULT_BYTE_SIZE = 1000;

    public static String slurp(String path) throws IOException {
        return slurp(new FileInputStream(path));
    }

    public static String slurp(InputStream in) throws IOException {
        return slurp(in, DEFAULT_BYTE_SIZE);
    }

    public static String slurp(InputStream in, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        StringBuilder sb = new StringBuilder();
        while (in.read(buffer) != -1) {
            sb.append(new String(buffer));
            buffer = new byte[bufferSize];
        }
        in.close();
        return sb.toString();
    }

    public static void spit(String path, String data) throws IOException {
        spit(new FileOutputStream(path), data.getBytes());
    }

    public static void spit(String path, byte[] data) throws IOException {
        spit(new FileOutputStream(path), data);
    }

    public static void spit(OutputStream out, byte[] data) throws IOException {
        out.write(data);
        out.close();
    }

}