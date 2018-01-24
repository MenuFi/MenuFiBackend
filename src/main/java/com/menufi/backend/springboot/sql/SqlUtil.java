package com.menufi.backend.springboot.sql;

import com.google.common.base.Joiner;

import java.util.*;

public class SqlUtil {

    public static String queryResultsToString(List<Map<String,String>> results) {
        if (results == null) {
            return "Results were NULL, Check for error logs";
        } else if (results.size() == 0) {
            return "Results were empty";
        }
        List<String> columns = new ArrayList<>(results.get(0).keySet());
        List<String> printColumns = new ArrayList<>();
        for (String val: results.get(0).keySet()) {
            printColumns.add(String.format("%-30.30s", val));
        }

        StringBuilder toStringValue = new StringBuilder();
        toStringValue.append(Joiner.on("\t").join(printColumns));
        toStringValue.append("\n");
        for (Map<String, String> entry: results) {
            List<String> values = new ArrayList<>();
            for (String col: columns) {
                values.add(String.format("%-30.30s", entry.get(col)));
            }
            toStringValue.append(Joiner.on("\t").join(values));
            toStringValue.append("\n");
        }
        return toStringValue.toString();
    }
}
