package com.menufi.backend.sql;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class CloudSqlQuerier implements Querier {
    private static Logger logger = Logger.getLogger("SqlLogger");
    private static boolean initialized = false;
    private static CloudSqlQuerier querier;
    private Connection conn;

    private CloudSqlQuerier() {}

    public static CloudSqlQuerier getSqlQuerier() {
        if (!initialized) {
            querier = new CloudSqlQuerier();
            initialized = querier.initialize();
        }
        return querier;
    }

    @Override
    public Map<String, String> query(String table, List<String> columns, Map<String, String> where) {
        return null;
    }

    @Override
    public ResultSet rawQuery(String query) {
        try {
            return conn.prepareStatement(query).executeQuery();
        } catch (Exception e) {
            logger.severe("SQL Query Failed");
            logger.severe(e.getMessage());
            return null;
        }
    }

    @Override
    public boolean insert(String table, Map<String, String> values) {
        return false;
    }

    @Override
    public boolean rawInsert(String query) {
        return false;
    }

    private boolean initialize() {
        String url;
        if (System.getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
            url = System.getProperty("ae-cloudsql.cloudsql-database-url");
            logger.log(Level.INFO, "Connecting to remote Google SQL instance");
            try {
                Class.forName("com.mysql.jdbc.GoogleDriver");
            } catch (ClassNotFoundException e) {
                logger.log(Level.SEVERE, "Missing GoogleDriver for SQL check your dependencies");
            }
        } else {
            url = System.getProperty("ae-cloudsql.local-database-url");
            logger.log(Level.INFO, "Connecting to local Google SQL instance");
        }
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Failed to connect to Google SQL");
            logger.log(Level.SEVERE, e.getMessage());
            return false;
        }
        return true;
    }

}
