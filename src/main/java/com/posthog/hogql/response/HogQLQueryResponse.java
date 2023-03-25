package com.posthog.hogql.response;
import java.util.List;

public class HogQLQueryResponse {
    private String query;
    private String hogql;
    private String clickhouse;
    private List<List<Object>> results;
    private List<List<String>> types;
    private List<String> columns;

    // Getters and setters for each field
    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getHogql() {
        return hogql;
    }

    public void setHogql(String hogql) {
        this.hogql = hogql;
    }

    public String getClickhouse() {
        return clickhouse;
    }

    public void setClickhouse(String clickhouse) {
        this.clickhouse = clickhouse;
    }

    public List<List<Object>> getResults() {
        return results;
    }

    public void setResults(List<List<Object>> results) {
        this.results = results;
    }

    public List<List<String>> getTypes() {
        return types;
    }

    public void setTypes(List<List<String>> types) {
        this.types = types;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }
}
