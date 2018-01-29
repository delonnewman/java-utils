package edu.cnm.util;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.util.*;
import java.util.logging.*;

public final class BannerConnection {
    // Retrieve logger and log class name for troubleshooting
    private static final String CLASS_NAME           = BannerConnection.class.getName();
    private static final Logger LOG                  = Logger.getLogger(CLASS_NAME);

    private static final String GLOBAL_NAME_QUERY    = "select * from global_name";

    // environment variables for late-bound credential setting
    private static final String PASSWORD_ENV         = "PPW";
    private static final String JDBC_CONNECTION_ENV  = "JDBC_CONNECTION";

    private static final String CONFIG_FILE          = "config.properties";

    // config properties
    private static final String USER_PROP            = "db.user";
    private static final String PASSWORD_PROP        = "db.password";
    private static final String JDBC_CONNECTION_PROP = "db.jdbc.connection";

    private static Connection connection;
    private static String globalName;

    public static String getGlobalName() throws Exception {
        if (globalName == null) {
            Long start = System.currentTimeMillis();
            globalName = (String) DBUtils.firstRow(getConnection(), GLOBAL_NAME_QUERY).get("GLOBAL_NAME");
            Long dur = System.currentTimeMillis() - start;
            LOG.info("[SQLStats] GLOBAL_NAME[] = " + globalName + " in " + dur + " ms");
        }

        return globalName;
    }

    public static Properties getConfig() throws Exception {
        File file = new File(BannerConnection.class.getClassLoader().getResource(CONFIG_FILE).toURI());
        LOG.log(Level.INFO, "file-path: " + file.getAbsolutePath());
        if (file.exists()) {
            Properties props = new Properties();
            props.load(new FileInputStream(file));
            return props;
        }
        else {
            throw new Exception("Cannot find config file: '" + CONFIG_FILE + "'");
        }
    }

    public static Connection getConnection() throws Exception {
        if (connection == null) {
            String     password = System.getenv(PASSWORD_ENV);
            Properties props    = getConfig();
            String     user     = props.getProperty(USER_PROP);

            if (password == null) {
                password = props.getProperty(PASSWORD_PROP);
                if (password == null) {
                    LOG.log(Level.SEVERE, PASSWORD_ENV + " not defined or password not set in " + CONFIG_FILE + "; unable to connect to Banner database.");
                    throw new IllegalArgumentException(PASSWORD_ENV + " not defined");
                }
            }

            String url = System.getenv(JDBC_CONNECTION_ENV);

            if (url != null) {
                LOG.log(Level.FINE, "Using JDBC connection '" + url + "'");
            } else {
                url = props.getProperty(JDBC_CONNECTION_PROP);
                LOG.log(Level.INFO, JDBC_CONNECTION_ENV + " not defined; using default '" + url + "'");
            }

            Long start = System.currentTimeMillis();
            connection = DriverManager.getConnection(url, user, password);
            Long dur = System.currentTimeMillis() - start;
            LOG.info("[SQLStats] CONNECT[" + url + " as " + user + "] in " + dur + " ms");
        }

        return connection;
    }

    public static boolean isProduction() {
        return globalName.toLowerCase().startsWith("prod");
    }

    private BannerConnection() {
        throw new IllegalArgumentException("Cannot instantiate BannerConnection; use static getConnection()");
    }
}
