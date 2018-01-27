package com.menufi.backend.springboot;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.menufi.backend.springboot.sql.CloudSqlQuerier;
import com.menufi.backend.springboot.sql.MockQuerier;
import com.menufi.backend.springboot.sql.Querier;
import com.menufi.backend.springboot.sql.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

    @Autowired
    @Qualifier("CloudSqlQuerier")
    private Querier querier;

    private String sqlTest() {
        //Map<String, String> newEntry = ImmutableMap.of("Email", "fakeEmail@gmail.com", "PasswordHash","1995");
        //querier.insert("patron_login", newEntry);

        //Map<String, String> update = ImmutableMap.of("PasswordHash", "773");
        //Map<String, String> where = ImmutableMap.of("UserId", "2");
        //querier.update("patron_login", update, where);

        Map<String, String> where = ImmutableMap.of("UserId", "1");
        List<String> cols = ImmutableList.of("Email", "PasswordHash");

        List<Map<String,String>> result1 = querier.query("patron_login", null);
        List<Map<String,String>> result2 = querier.query("patron_login", cols);
        List<Map<String,String>> result3 = querier.queryWhere("patron_login", null, where);
        List<Map<String,String>> result4 = querier.queryWhere("patron_login", cols, where);
        System.out.println("\nTest 1");
        System.out.println(SqlUtil.queryResultsToString(result1));
        System.out.println("\nTest 2");
        System.out.println(SqlUtil.queryResultsToString(result2));
        System.out.println("\nTest 3");
        System.out.println(SqlUtil.queryResultsToString(result3));
        System.out.println("\nTest 4");
        System.out.println(SqlUtil.queryResultsToString(result4));
        return SqlUtil.queryResultsToString(result1);
    }
}
