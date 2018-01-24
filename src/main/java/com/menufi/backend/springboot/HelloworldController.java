package com.menufi.backend.springboot;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.menufi.backend.springboot.sql.CloudSqlQuerier;
import com.menufi.backend.springboot.sql.MockQuerier;
import com.menufi.backend.springboot.sql.Querier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class HelloworldController {
    @GetMapping("/")
    public String hello() {
        return sqlTest();
    }

    private String sqlTest() {
        boolean useMock = false;
        if (useMock) {
            Querier querier = new MockQuerier();
            querier.insert("Test", ImmutableMap.of("name", "Charlie"));
            List<Map<String,String>> result = querier.query("Test", ImmutableList.of("name"));
            return result.get(0).get("name");
        } else {
            Querier querier = CloudSqlQuerier.getSqlQuerier();
            List<Map<String,String>> result = querier.query("patron_login", null);
            return result.get(0).get("Email");
        }
    }
}
