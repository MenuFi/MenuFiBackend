package com.menufi.backend.springboot;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.menufi.backend.sql.CloudSqlQuerier;
import com.menufi.backend.sql.MockQuerier;
import com.menufi.backend.sql.Querier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HelloworldController {
    @GetMapping("/")
    public String hello() {
        return sqlTest();
    }

    private String sqlTest() {
        String query = "SELECT * FROM patron_login;";
        Querier querier = new MockQuerier();
        List<Map<String,String>> result = querier.query("Test", ImmutableList.of("name"));
        return result.get(0).get("name");
    }
}
