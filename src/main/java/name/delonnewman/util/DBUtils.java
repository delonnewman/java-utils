package edu.cnm.util;

import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBUtils {
    private static final Logger LOG = Logger.getLogger("DBUtils");

    public static List<Map<String, Object>> rows(Connection conn, String query) throws Exception {
        List<Object> params = new ArrayList<>();
        return rows(conn, query, params);
    }

    public static List<Map<String, Object>> rows(Connection conn, String query, List params) throws Exception {
        List<Map<String, Object>> result;
        PreparedStatement stmt = conn.prepareStatement(query);

        try {
            LOG.log(Level.INFO, "Fetching all rows of: " + query + " PARAMS: " + fmtParams(params));
            result = rows(stmt, params);
        }
        catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        finally {
            stmt.close();
        }

        return result;
    }

    public static List<Map<String, Object>> rows(PreparedStatement stmt, List params) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();

        if (params.size() != 0) setParameters(stmt, params);
        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        List<String> fields = new ArrayList<>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String column = meta.getColumnName(i);
            fields.add(column);
        }
        while (rs.next()) {
            Map<String, Object> row = new TreeMap<>(); // preserves order
            for (String field: fields) {
                row.put(field, rs.getObject(field));
            }
            result.add(row);
        }

        stmt.close();

        return result;
    }

    public static PreparedStatement prepareStatement(Connection conn, String query) throws Exception {
        LOG.log(Level.INFO, "Preparing statement SQL: " + query);
        return conn.prepareStatement(query);
    }

    public static PreparedStatement prepareStatementOrDie(Connection conn, String query) {
        PreparedStatement stmt = null;

        try {
            stmt = prepareStatement(conn, query);
        }
        catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
            System.exit(1);
        }

        return  stmt;
    }

    public static PreparedStatement setParameters(PreparedStatement stmt, List params) throws Exception {
        for (int i = 1; i <= params.size(); i++) {
            Object param = params.get(i - 1);
            stmt.setObject(i, param);
        }
        return stmt;
    }

    public static Map<String, Object> firstRow(Connection conn, String query) throws Exception {
        List<Object> params = new ArrayList<>();
        return firstRow(conn, query, params);
    }

    public static Map<String, Object> firstRow(Connection conn, String query, Object x) throws Exception {
        List<Object> params = new ArrayList<>();
        params.add(x);
        return firstRow(conn, query, params);
    }

    public static Map<String, Object> firstRow(Connection conn, String query, Object x, Object y) throws Exception {
        List<Object> params = new ArrayList<>();
        params.add(x);
        params.add(y);
        return firstRow(conn, query, params);
    }

    public static Map<String, Object> firstRow(Connection conn, String query, Object x, Object y, Object z) throws Exception {
        List<Object> params = new ArrayList<>();
        params.add(x);
        params.add(y);
        params.add(z);
        return firstRow(conn, query, params);
    }

    private static String fmtParams(List params) {
        StringBuffer sb = new StringBuffer();
        for (Object param: params) {
            sb.append(param.toString());
        }
        return sb.toString();
    }

    public static Map<String, Object> firstRow(Connection conn, String query, List<Object> params) throws Exception {
        Map<String, Object> row;

        PreparedStatement stmt = conn.prepareStatement(query);

        try {
            LOG.log(Level.INFO, "Fetching first row of: " + query + " PARAMS: " + fmtParams(params));
            row = firstRow(stmt, params);
        }
        catch (Exception e) {
            LOG.log(Level.SEVERE, e.getMessage());
            throw e;
        }
        finally {
            stmt.close();
        }

        return row;
    }

    public static Map<String, Object> firstRow(PreparedStatement stmt, List<Object> params) throws Exception {
        Map<String, Object> row = new TreeMap<>(); // preserves order

        if (params.size() != 0) setParameters(stmt, params);
        ResultSet rs = stmt.executeQuery();
        ResultSetMetaData meta = rs.getMetaData();
        List<String> fields = new ArrayList<>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            fields.add(meta.getColumnName(i));
        }
        rs.next();

        for (String field: fields) {
            row.put(field, rs.getObject(field));
        }

        return row;
    }

    public static String mkParamList(int n) {
        StringBuffer sb = new StringBuffer("(");
        for (int i = 0; i < n; i++) {
            if (i == n - 1) {
                sb.append("?");
            }
            else {
                sb.append("?,");
            }
        }
        sb.append(")");

        return sb.toString();
    }
}
