package com.menufi.backend.springboot.sql;

import com.google.common.base.Joiner;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service("CloudSqlQuerier")
@Scope(value= ConfigurableBeanFactory.SCOPE_SINGLETON)
@Profile("default")
public class CloudSqlQuerier implements Querier {
    private static final String INSERT_FORMAT = "INSERT INTO %s (%s) VALUES (%s);";
    private static final String SELECT_FORMAT = "SELECT %s FROM %s";
    private static final String SELECT_WHERE_FORMAT = "SELECT %s FROM %s WHERE %s;";
    private static final String UPDATE_FORMAT = "UPDATE %s SET %s WHERE %s;";
    private static final String DELETE_FORMAT = "DELETE FROM %s WHERE %s";

    private static Logger logger = Logger.getLogger("SqlLogger");
    private Connection conn;

    public CloudSqlQuerier() {
        initialize();
    }

    @Override
    public List<Map<String, String>> query(String table, List<String> columns) {
        String columnString = columns == null ? "*" : Joiner.on(",").join(columns);
        String query = String.format(SELECT_FORMAT, columnString, table);
        return executeSelect(query);
    }

    @Override
    public List<Map<String, String>> queryWhere(String table, List<String> columns, Map<String, String> where) {
        List<String> whereConditionList = new ArrayList<>();
        for (String colName: where.keySet()) {
            whereConditionList.add(String.format("%s='%s'", colName, where.get(colName)));
        }
        String whereString = Joiner.on(" and ").join(whereConditionList);
        String columnString = columns == null ? "*" : Joiner.on(",").join(columns);
        String query = String.format(SELECT_WHERE_FORMAT, columnString, table, whereString);
        return executeSelect(query);
    }

    private List<Map<String, String>> executeSelect(String query) {
        List<Map<String, String>> result = new ArrayList<>();
        ResultSet rs;
        try {
            logger.info("Executing query: " + query);
            rs = conn.prepareStatement(query).executeQuery();
        } catch (SQLException e) {
            logger.severe("Unable to execute query: " + query);
            logger.severe(e.getMessage());
            return null;
        }
        try {
            ResultSetMetaData md = rs.getMetaData();
            while (rs.next()) {
                HashMap<String, String> entry = new HashMap<>();
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    entry.put(md.getColumnLabel(i), rs.getString(i));
                }
                result.add(entry);
            }
        } catch (SQLException e) {
            logger.severe("Error while parsing query result");
            logger.severe(e.toString());
        }
        return result;
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
        List<String> colNamesList = new ArrayList<>();
        List<String> colValuesList = new ArrayList<>();
        for (String colName: values.keySet()) {
            colNamesList.add(colName);
            colValuesList.add(String.format("'%s'", values.get(colName)));
        }
        String colNames = Joiner.on(",").join(colNamesList);
        String colValues = Joiner.on(",").join(colValuesList);
        String query = String.format(INSERT_FORMAT, table, colNames, colValues);
        try {
            logger.info("Executing insert: " + query);
            conn.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error while executing insert statement: " + query);
            logger.severe(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean rawInsert(String query) {
        try {
            conn.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error while executing sql insert statement");
            logger.severe(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public boolean update(String table, Map<String, String> updates, Map<String, String> where) {
        List<String> whereConditionList = new ArrayList<>();
        for (String colName: where.keySet()) {
            whereConditionList.add(String.format("%s='%s'", colName, where.get(colName)));
        }
        String whereString = Joiner.on(" and ").join(whereConditionList);

        List<String> updatesList = new ArrayList<>();
        for (String colName: updates.keySet()) {
            updatesList.add(String.format("%s='%s'", colName, updates.get(colName)));
        }
        String updatesString = Joiner.on(", ").join(updatesList);

        String query = String.format(UPDATE_FORMAT, table, updatesString, whereString);
        try {
            logger.info("Executing update: " + query);
            return 0 != conn.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error while executing sql update statement: " + query);
            logger.severe(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean delete(String table, Map<String, String> where) {
        List<String> whereConditionList = new ArrayList<>();
        for (String colName: where.keySet()) {
            whereConditionList.add(String.format("%s='%s'", colName, where.get(colName)));
        }
        String whereString = Joiner.on(" and ").join(whereConditionList);

        String query = String.format(DELETE_FORMAT, table, whereString);
        try {
            logger.info("Executing delete: " + query);
            conn.prepareStatement(query).executeUpdate();
        } catch (SQLException e) {
            logger.severe("Error while executing sql delete statement: " + query);
            logger.severe(e.getMessage());
            return false;
        }
        return true;

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
