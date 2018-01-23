package com.menufi.backend.springboot;

import com.menufi.backend.sql.CloudSqlQuerier;
import com.menufi.backend.sql.Querier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;

@RestController
public class HelloworldController {
    @GetMapping("/")
    public String hello() {
        return sqlTest();
    }

    private String sqlTest() {
        String query = "SELECT * FROM patron_login;";
        Querier querier = CloudSqlQuerier.getSqlQuerier();
        ResultSet rs = querier.rawQuery(query);
        try {
            rs.next();
            return rs.getString("email");
        } catch (Exception e) {}
        return "Failure";
    }
}
