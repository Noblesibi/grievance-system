package com.saintgits.sgs.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for obtaining JDBC connections.
 * Reads configuration from /db.properties on the classpath.
 */
public class DBConnection {

    private static final Properties PROPS = new Properties();
    private static String url;
    private static String user;
    private static String password;

    static {
        try (InputStream is = DBConnection.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (is != null) {
                PROPS.load(is);
            }
            url      = PROPS.getProperty("db.url",      "jdbc:mysql://localhost:3306/sgs_db?useSSL=false&serverTimezone=Asia/Kolkata&allowPublicKeyRetrieval=true");
            user     = PROPS.getProperty("db.user",     "root");
            password = PROPS.getProperty("db.password", "");
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (IOException | ClassNotFoundException e) {
            throw new ExceptionInInitializerError("DBConnection init failed: " + e.getMessage());
        }
    }

    /**
     * Returns a new JDBC connection. Caller is responsible for closing it.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
