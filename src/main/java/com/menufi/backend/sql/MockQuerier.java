package com.menufi.backend.sql;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockQuerier implements Querier {
    private Map<String, List<Map<String, String>>> db = new HashMap<>();

    @Override
    public List<Map<String, String>> query(String table, List<String> columns) {
        List<Map<String,String>> dbTable = db.get(table);
        List<Map<String,String>> result = new ArrayList<>();
        for (Map<String, String> entry : dbTable) {
            if (columns == null) {
                result.add(entry);
            } else {
                Map<String, String> returnEntry = new HashMap<>();
                for (String col : columns) {
                    if (entry.containsKey(col)) {
                        returnEntry.put(col, entry.get(col));
                    }
                }
                result.add(returnEntry);
            }
        }
        return result;
    }

    @Override
    public List<Map<String, String>> queryWhere(String table, List<String> columns, Map<String, String> where) {
        List<Map<String,String>> dbTable = db.get(table);
        List<Map<String,String>> result = new ArrayList<>();
        for (Map<String, String> row: dbTable) {
            boolean matches = true;
            for (String colName: where.keySet()) {
                matches = row.containsKey(colName) && matches && row.get(colName).equals(where.get(colName));
            }
            if (matches) {
                if (columns == null) {
                    result.add(row);
                } else {
                    Map<String, String> returnEntry = new HashMap<>();
                    for (String col : columns) {
                        if (row.containsKey(col)) {
                            returnEntry.put(col, row.get(col));
                        }
                    }
                    result.add(returnEntry);
                }
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
        if (!db.containsKey(table)) {
            db.put(table, new ArrayList<>());
        }
        db.get(table).add(values);
        return true;
    }

    @Override
    public boolean rawInsert(String query) {
        throw new UnsupportedOperationException();
    }
}
