package com.menufi.backend.springboot.sql;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service("MockQuerier")
@Scope(value= ConfigurableBeanFactory.SCOPE_SINGLETON)
@Profile("test")
public class MockQuerier implements Querier {
    private Map<String, List<Map<String, String>>> db = new HashMap<>();

    @Override
    public List<Map<String, String>> query(String table, List<String> columns) {
        List<Map<String,String>> dbTable = db.get(table);
        List<Map<String,String>> result = new ArrayList<>();
        if (dbTable == null) {
            Logger.getLogger("MockQuerier").severe("Queried table does not exist: " + table);
            return result;
        }
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
        if (dbTable == null) {
            Logger.getLogger("MockQuerier").severe("Queried table does not exist: " + table);
            return result;
        }
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
        db.get(table).add(new HashMap<>(values));
        return true;
    }

    @Override
    public boolean rawInsert(String query) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean update(String table, Map<String, String> updates, Map<String, String> where) {
        List<Map<String, String>> dbTable = db.get(table);
        for (Map<String, String> entry : dbTable) {
            if (entryMatchesWhere(entry, where)) {
                mergeMaps(entry, updates);
            }
        }
        return true;
    }

    private void mergeMaps(Map<String, String> first, Map<String, String> second) {
        for (String col : second.keySet()) {
            first.put(col, second.get(col));
        }
    }

    private boolean entryMatchesWhere(Map<String, String> entry, Map<String, String> where) {
        for (String col : where.keySet()) {
            if (!entry.containsKey(col) || !where.get(col).equals(entry.get(col))){
                return false;
            }
        }
        return true;
    }
}
