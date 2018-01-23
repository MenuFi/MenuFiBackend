package com.menufi.backend.sql;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

public interface SqlQuerier {

    Map<String, String> query(String table, List<String> columns, Map<String, String> where);
    @Deprecated
    ResultSet rawQuery(String query);
    boolean insert(String table, Map<String, String> values);
    @Deprecated
    boolean rawInsert(String query);

}
