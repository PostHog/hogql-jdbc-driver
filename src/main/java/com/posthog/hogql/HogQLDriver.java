package com.posthog.hogql;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.TimeZone;

public class HogQLDriver implements java.sql.Driver {

    static {
        try {
            DriverManager.registerDriver(new HogQLDriver());
            System.out.println("Registering HogQLDriver");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to register HogQLDriver", e);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        if (!acceptsURL(url)) {
            return null;
        }
        return new HogQLConnection(url, info);
    }

    @Override
    public boolean acceptsURL(String url) {
        return url != null && url.startsWith("jdbc:hogql:");
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
        // Return an array of DriverPropertyInfo objects that describe the properties
        // supported by this driver.
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        // Return the major version number of this driver.
        return 1;
    }

    @Override
    public int getMinorVersion() {
        // Return the minor version number of this driver.
        return 0;
    }

    @Override
    public boolean jdbcCompliant() {
        // Indicates whether this driver is JDBC compliant.
        return false;
    }

    @Override
    public Logger getParentLogger() {
        // Return the parent logger for this driver.
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }
}
