package com.menufi.backend.springboot.sql;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MockQuerierTest {
    private MockQuerier querier = new MockQuerier();

    private static final String table1 = "FakeTable1";
    private static final String col1 = "name";
    private static final String col1val1 = "Charlie";
    private static final String col1val2 = "Lulu";
    private static final String col2 = "num";
    private static final String col2val1 = "5";
    private static final String col2val2 = "6";
    private static final Map<String, String> entry1 = ImmutableMap.of(col1, col1val1, col2, col2val1);
    private static final Map<String, String> entry2 = ImmutableMap.of(col1, col1val2, col2,col2val2);

    @Before
    public void insertMockData() {
        querier.insert(table1, entry1);
        querier.insert(table1, entry2);
    }

    @Test
    public void testQueryAll() {
        List<Map<String, String>> expected = ImmutableList.of(entry1, entry2);
        List<Map<String, String>> result = querier.query(table1, null);
        assertEquals(expected, result);
    }

    @Test
    public void testQueryWhereAll() {
        List<Map<String, String>> expected = ImmutableList.of(entry2);
        List<Map<String, String>> result = querier.queryWhere(table1, null, ImmutableMap.of(col2, col2val2));
        assertEquals(expected, result);
    }

    @Test
    public void testQueryColWhere() {
        Map<String, String> tempEntry2 = ImmutableMap.of(col1, col1val2);
        List<Map<String, String>> expected = ImmutableList.of(tempEntry2);
        List<Map<String, String>> result = querier.queryWhere(table1, ImmutableList.of(col1), ImmutableMap.of(col1, col1val2));
        assertEquals(expected, result);
    }

    @Test
    public void testQueryCol() {
        Map<String, String> tempEntry1 = ImmutableMap.of(col1, col1val1);
        Map<String, String> tempEntry2 = ImmutableMap.of(col1, col1val2);
        List<Map<String, String>> expected = ImmutableList.of(tempEntry1, tempEntry2);
        List<Map<String, String>> result = querier.query(table1, ImmutableList.of(col1));
        assertEquals(expected, result);
    }

    @Test
    public void testUpdate() {
        String updateValue = "Charles";
        Map<String, String> updatedEntry = ImmutableMap.of(col1, updateValue, col2, col2val1);
        Map<String, String> updates = ImmutableMap.of(col1, updateValue);
        Map<String, String> where = ImmutableMap.of(col2, col2val1);
        querier.update(table1, updates, where);
        List<Map<String, String>> result = querier.query(table1, null);
        List<Map<String, String>> expected = ImmutableList.of(updatedEntry, entry2);
        assertEquals(expected, result);

    }
}
