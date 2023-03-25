import org.junit.jupiter.api.Test;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.posthog.hogql.HogQLDriver;

import static org.junit.jupiter.api.Assertions.*;

class HogQLDriverTest {

    @Test
    void testDriverRegistration() {
        assertDoesNotThrow(() -> {
            Driver driver = DriverManager.getDriver("jdbc:hogql://localhost");
            assertNotNull(driver);
        });
    }

    @Test
    void testDriverAcceptsUrl() throws SQLException {
        Driver driver = new HogQLDriver();
        assertTrue(driver.acceptsURL("jdbc:hogql://localhost"));
        assertFalse(driver.acceptsURL("jdbc:mysql://localhost"));
    }
}