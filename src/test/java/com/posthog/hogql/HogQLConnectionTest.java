package com.posthog.hogql;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class HogQLConnectionTest {

    @Test
    void testConnectionStringEmpty() throws SQLException {
        HogQLConnection connection = new HogQLConnection("", new Properties());
        assertNull(connection.getApiKey());
        assertNull(connection.getApiUrl());
    }

    @Test
    void testConnectionStringPartial() throws SQLException {
        HogQLConnection connection = new HogQLConnection("jdbc:hogql://API_KEY@app.posthog.com", new Properties());
        assertNull(connection.getApiKey());
        assertNull(connection.getApiUrl());
    }

    @Test
    void testConnectionStringNoUser() throws SQLException {
        HogQLConnection connection = new HogQLConnection("jdbc:hogql://app.posthog.com/PROJECT_ID", new Properties());
        assertNull(connection.getApiKey());
        assertNull(connection.getApiUrl());
    }

    @Test
    void testConnectionStringFull() throws SQLException {
        HogQLConnection connection = new HogQLConnection("jdbc:hogql://API_KEY@app.posthog.com/PROJECT_ID", new Properties());
        assertEquals(connection.getApiKey(), "API_KEY");
        assertEquals(connection.getApiUrl(), "https://app.posthog.com/api/projects/PROJECT_ID/query");
    }

    @Test
    void testConnectionStringFullExtraSlash() throws SQLException {
        HogQLConnection connection = new HogQLConnection("jdbc:hogql://API_KEY@app.posthog.com/PROJECT_ID/", new Properties());
        assertEquals(connection.getApiKey(), "API_KEY");
        assertEquals(connection.getApiUrl(), "https://app.posthog.com/api/projects/PROJECT_ID/query");
    }

    @Test
    void testConnectionStringWithPort() throws SQLException {
        HogQLConnection connection = new HogQLConnection("jdbc:hogql://API_KEY@myhostname:8080/PROJECT_ID/", new Properties());
        assertEquals(connection.getApiKey(), "API_KEY");
        assertEquals(connection.getApiUrl(), "http://myhostname:8080/api/projects/PROJECT_ID/query");
    }

    @Test
    void testConnectionStringWithPort443() throws SQLException {
        HogQLConnection connection = new HogQLConnection("jdbc:hogql://API_KEY@myhostname:443/PROJECT_ID/", new Properties());
        assertEquals(connection.getApiKey(), "API_KEY");
        assertEquals(connection.getApiUrl(), "https://myhostname/api/projects/PROJECT_ID/query");
    }

    @Test
    void testConnectionStringWithPort8443() throws SQLException {
        HogQLConnection connection = new HogQLConnection("jdbc:hogql://API_KEY@myhostname:8443/PROJECT_ID/", new Properties());
        assertEquals(connection.getApiKey(), "API_KEY");
        assertEquals(connection.getApiUrl(), "https://myhostname:8443/api/projects/PROJECT_ID/query");
    }

}