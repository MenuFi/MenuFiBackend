package com.menufi.backend.springboot.sql;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public interface Querier {

    List<Map<String, String>> query(String table, List<String> columns);
    List<Map<String, String>> queryWhere(String table, List<String> columns, Map<String, String> where);
    @Deprecated
    ResultSet rawQuery(String query);
    boolean insert(String table, Map<String, String> values);
    @Deprecated
    boolean rawInsert(String query);
    boolean update(String table, Map<String, String> updates, Map<String, String> where);

}
