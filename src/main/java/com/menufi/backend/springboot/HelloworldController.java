package com.menufi.backend.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
public class HelloworldController {
    private Connection conn;
    private boolean initialized = false;

    @GetMapping("/")
    public String hello() {
        return sqlTest();
    }

    private String sqlTest() {
        if (!initialized) {
            initConnection();
            initialized = true;
        }
        String response = "No Response";
        String query = "SELECT * FROM patron_login;";
        try {
            ResultSet rs = conn.prepareStatement(query).executeQuery();
            rs.next();
            response = rs.getString("email");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return response;
    }

    private void initConnection() {
        try {
            String url;
            if (System.getProperty("com.google.appengine.runtime.version").startsWith("Google App Engine/")) {
                // Check the System properties to determine if we are running on appengine or not
                // Google App Engine sets a few system properties that will reliably be present on a remote
                // instance.
                url = System.getProperty("ae-cloudsql.cloudsql-database-url");
                // Load the class that provides the new "jdbc:google:mysql://" prefix.
                Class.forName("com.mysql.jdbc.GoogleDriver");
            } else {
                // Set the url with the local MySQL database connection url when running locally
                url = System.getProperty("ae-cloudsql.local-database-url");
            }
            Logger.getGlobal().log(Level.SEVERE, url);
            conn = DriverManager.getConnection(url);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
