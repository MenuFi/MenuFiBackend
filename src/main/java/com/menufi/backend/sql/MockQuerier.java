package com.menufi.backend.sql;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockQuerier implements Querier {
    private Map<String, List<Map<String, String>>> db = new HashMap<>();

    @Override
    public List<Map<String, String>> query(String table, List<String> columns, Map<String, String> where) {
        List<Map<String,String>> dbTable = db.get(table);
        List<Map<String,String>> result = new ArrayList<>();
        for (Map<String, String> row: dbTable) {
            boolean matches = true;
            for (String colName: where.keySet()) {
                if (row.containsKey(colName)) {
                    matches = matches && row.get(colName).equals(where.get(colName));
                } else {
                    matches = false;
                }
            }
            if (matches) {
                result.add(row);
            }
        }
        return result;
    }

    @Override
    public ResultSet rawQuery(String query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean insert(String table, Map<String, String> values) {
        db.get(table).add(values);
        return true;
    }

    @Override
    public boolean rawInsert(String query) {
        throw new UnsupportedOperationException();
    }
}
