package com.posthog.hogql.response;
import com.posthog.hogql.response.HogQLQueryResponse;
import com.google.gson.Gson;
import com.mockrunner.jdbc.JDBCTestModule;
import com.mockrunner.mock.jdbc.MockResultSet;
import java.io.InputStream;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class JSONToResultSet {
    public static ResultSet jsonToResultSet(String jsonString) throws SQLException {
        // Parse JSON string into HogQLQueryResponse object
        Gson gson = new Gson();
        HogQLQueryResponse response = gson.fromJson(jsonString, HogQLQueryResponse.class);

        // Create a MockResultSet from the response
        MockResultSet resultSet = new MockResultSet("HogQLResultSet");

        // Add column names to the ResultSet
        for (String columnName : response.getColumns()) {
            resultSet.addColumn(columnName);
        }

        // Add rows to the ResultSet
        for (List<Object> row : response.getResults()) {
            resultSet.addRow(row);
        }

        return resultSet;
    }

    public static ResultSet inputStreamToResultSet(InputStream inputStream) throws SQLException, IOException {
        return jsonToResultSet(readInputStreamAsString(inputStream));
    }

    private static String readInputStreamAsString(InputStream is) throws IOException {
        int bufferSize = 65536;
        ByteArrayOutputStream result = new ByteArrayOutputStream();
        byte[] buffer = new byte[bufferSize];
        int length;
        while ((length = is.read(buffer)) != -1) {
            result.write(buffer, 0, length);
        }
        return result.toString(StandardCharsets.UTF_8.name());
    }

}
