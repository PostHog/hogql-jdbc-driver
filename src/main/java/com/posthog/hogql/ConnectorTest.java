package com.posthog.hogql;
import com.posthog.hogql.HogQLDriver;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Properties;


public class ConnectorTest {

    public static void main(String[] args) throws Exception {
        System.out.println("Hello World");

        Driver driver = new HogQLDriver();
        Connection connection = driver.connect("jdbc:hogql://localhost", new Properties());

        ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM events");
        System.out.println(rs);

    }
}