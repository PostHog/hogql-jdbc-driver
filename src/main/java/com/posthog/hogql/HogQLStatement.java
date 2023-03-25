package com.posthog.hogql;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.EnumMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.TimeZone;
import com.posthog.hogql.util.guava.StreamUtils;
import com.posthog.hogql.ClickHouseExternalData;
import com.posthog.hogql.domain.ClickHouseFormat;
import com.posthog.hogql.response.AbstractResultSet;
import com.posthog.hogql.response.JSONToResultSet;
import com.posthog.hogql.settings.ClickHouseProperties;
import com.posthog.hogql.settings.ClickHouseQueryParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;


public class HogQLStatement implements java.sql.Statement {
    private static final Logger log = LoggerFactory.getLogger(HogQLStatement.class);

    private final HogQLConnection connection;
    private boolean closeOnCompletion;
    private int currentUpdateCount = -1;
    private int queryTimeout = -1;
    private int maxRows;
    private volatile String queryId;
    private ClickHouseFormat selectFormat;
    private AbstractResultSet currentResult;
    private ClickHouseProperties properties;
    private final String initialDatabase;
    private static final String[] selectKeywords = new String[]{"SELECT", "WITH", "SHOW", "DESC", "EXISTS"};
    private static final String databaseKeyword = "CREATE DATABASE";

    public HogQLStatement(HogQLConnection connection) {
        this.connection = connection;
        this.properties = new ClickHouseProperties();
        this.initialDatabase = this.properties.getDatabase();
    }

    @Override
    public ResultSet executeQuery(String sql) throws SQLException {
        if (sql.trim().toLowerCase().startsWith("select")) {
            String apiUrl = "http://localhost:8000/api/projects/1/query"; // Replace with your API URL

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "Bearer phx_zBXe4XYk4UV5Rg4k0zZXcodlokNpiX4aHkWly7oaYwq");
                connection.setDoOutput(true);

                // Send the SELECT query as JSON payload
                Map<String, Object> queryMap = new HashMap<>();
                queryMap.put("kind", "HogQLQuery");
                queryMap.put("query", sql);
                this.queryId = ClickHouseUtil.generateQueryId();
                Map<String, Object> payloadMap = new HashMap<>();
                payloadMap.put("query", queryMap);
                String payload = new Gson().toJson(payloadMap);
                try (OutputStream os = connection.getOutputStream()) {
                    os.write(payload.getBytes("UTF-8"));
                }

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    currentUpdateCount = -1;
                    InputStream is = connection.getInputStream();
                    return JSONToResultSet.inputStreamToResultSet(is);
                } else {
                    System.out.println(responseCode);
                    throw new SQLException("Failed to execute SELECT query via PostHog HTTP API. Error code: " + responseCode + "");
                }
            } catch (IOException e) {
                throw new SQLException("Error sending SELECT query to PostHog HTTP API", e);
            }
        } else {
            // Handle non-SELECT queries
            return null;
        }
    }

    @Override
    public int executeUpdate(String sql) throws SQLException {
        return 0;
    }

    @Override
    public boolean execute(String sql) throws SQLException {
        executeQuery(sql);
        return true;
    }

    @Override
    public void close() throws SQLException {
        if (currentResult != null) {
            currentResult.close();
        }
    }

    @Override
    public int getMaxFieldSize() {
        return 0;
    }

    @Override
    public void setMaxFieldSize(int max) {

    }

    @Override
    public int getMaxRows() {
        return maxRows;
    }

    @Override
    public void setMaxRows(int max) throws SQLException {
        if (max < 0) {
            throw new SQLException(String.format("Illegal maxRows value: %d", max));
        }
        maxRows = max;
    }

    @Override
    public void setEscapeProcessing(boolean enable) {

    }

    @Override
    public int getQueryTimeout() {
        return queryTimeout;
    }

    @Override
    public void setQueryTimeout(int seconds) {
        queryTimeout = seconds;
    }

    @Override
    public void cancel() throws SQLException {
        if (this.queryId != null && !isClosed()) {
            executeQuery("KILL QUERY WHERE query_id='" + this.queryId + "'");
        }
    }

    @Override
    public SQLWarning getWarnings() {
        return null;
    }

    @Override
    public void clearWarnings() {

    }

    @Override
    public void setCursorName(String name) {

    }

    @Override
    public ResultSet getResultSet() {
        return currentResult;
    }

    @Override
    public int getUpdateCount() {
        return currentUpdateCount;
    }

    @Override
    public boolean getMoreResults() throws SQLException {
        if (currentResult != null) {
            currentResult.close();
            currentResult = null;
        }
        currentUpdateCount = -1;
        return false;
    }

    @Override
    public void setFetchDirection(int direction) {

    }

    @Override
    public int getFetchDirection() {
        return 0;
    }

    @Override
    public void setFetchSize(int rows) {

    }

    @Override
    public int getFetchSize() {
        return 0;
    }

    @Override
    public int getResultSetConcurrency() {
        return 0;
    }

    @Override
    public int getResultSetType() {
        return 0;
    }

    @Override
    public void addBatch(String sql) {

    }

    @Override
    public void clearBatch() {

    }

    @Override
    public int[] executeBatch() throws SQLException {
        return new int[0];
    }

    @Override
    public HogQLConnection getConnection() {
        return connection;
    }

    @Override
    public boolean getMoreResults(int current) {
        return false;
    }

    @Override
    public ResultSet getGeneratedKeys() {
        return null;
    }

    @Override
    public int executeUpdate(String sql, int autoGeneratedKeys) {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, int[] columnIndexes) {
        return 0;
    }

    @Override
    public int executeUpdate(String sql, String[] columnNames) {
        return 0;
    }

    @Override
    public boolean execute(String sql, int autoGeneratedKeys) {
        return false;
    }

    @Override
    public boolean execute(String sql, int[] columnIndexes) {
        return false;
    }

    @Override
    public boolean execute(String sql, String[] columnNames) {
        return false;
    }

    @Override
    public int getResultSetHoldability() {
        return 0;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public void setPoolable(boolean poolable) {

    }

    @Override
    public boolean isPoolable() {
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        if (iface.isAssignableFrom(getClass())) {
            return iface.cast(this);
        }
        throw new SQLException("Cannot unwrap to " + iface.getName());
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) {
        return iface.isAssignableFrom(getClass());
    }

    private static String toParamsString(List<SimpleImmutableEntry<String, String>> queryParams) {
        if (queryParams.isEmpty()) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (SimpleImmutableEntry<String, String> pair : queryParams) {
            sb.append(pair.getKey()).append("=").append(pair.getValue())
                    .append("&");
        }
        sb.setLength(sb.length() - 1); //remove last &
        return sb.toString();
    }

    public void closeOnCompletion() {
        closeOnCompletion = true;
    }

    public boolean isCloseOnCompletion() {
        return closeOnCompletion;
    }

    private static boolean detectQueryType(String sql) {
        for (int i = 0; i < sql.length(); i++) {
            String nextTwo = sql.substring(i, Math.min(i + 2, sql.length()));
            if ("--".equals(nextTwo)) {
                i = Math.max(i, sql.indexOf("\n", i));
            } else if ("/*".equals(nextTwo)) {
                i = Math.max(i, sql.indexOf("*/", i));
            } else if (Character.isLetter(sql.charAt(i))) {
                String trimmed = sql.substring(i, Math.min(sql.length(), Math.max(i, sql.indexOf(" ", i))));
                for (String keyword : selectKeywords) {
                    if (trimmed.regionMatches(true, 0, keyword, 0, keyword.length())) {
                        return true;
                    }
                }
                return false;
            }
        }
        return false;
    }
}
