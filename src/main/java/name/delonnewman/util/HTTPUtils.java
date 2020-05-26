package edu.cnm.util;

import java.io.*;
import java.net.URL;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import javax.net.ssl.HttpsURLConnection;

public final class HTTPUtils {

    public static final String USER_AGENT = "CNM HTTPUtils Client/1.1";

    public static String fmtParams(Map<String, Object> data) {
        List<String> pairs = new ArrayList<String>();
        for (Map.Entry<String, Object> kv: data.entrySet()) {
            pairs.add(kv.getKey() + "=\"" + kv.getValue().toString() + "\"");
        }
        return String.join("&", pairs);
    }

    public static Map request(String method, String baseUri) throws IOException {
        return request(method, baseUri, null, null);
    }

    public static Map request(String method, String baseUri, Map<String, Object> data) throws IOException {
        return request(method, baseUri, fmtParams(data), null);
    }

    public static Map request(String method, String baseUri, String data) throws IOException {
        return request(method, baseUri, data, null);
    }

    public static Map request(String method, String baseUri, String data, Map<String, String> headers) throws IOException {
        URL                 uri = new URL(baseUri);
        HttpsURLConnection conn = (HttpsURLConnection) uri.openConnection();

        conn.setRequestMethod(method);

        // headers
        if (headers != null) {
            for (Map.Entry<String, String> kv : headers.entrySet()) {
                conn.setRequestProperty(kv.getKey(), kv.getValue());
            }
        }

        // data
        if (data != null) {
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();
        }

        // build response map
        Map res = new HashMap<String, Object>();
        int code = conn.getResponseCode();

        res.put("code", code);
        res.put("request-method", method);
        res.put("request-uri", baseUri);
        res.put("request-data", data);
        res.put("request-headers", headers);

        if (code == HttpURLConnection.HTTP_OK) {
            res.put("status", "ok");
            res.put("body", conn.getInputStream());
        }
        else {
            res.put("status", "error");
            res.put("body", conn.getErrorStream());
        }

        return res;
    }
}
