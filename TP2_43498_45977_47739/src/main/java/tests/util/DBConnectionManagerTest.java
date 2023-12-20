package tests.util;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import util.DBConnectionManager;

class DBConnectionManagerTest {
    private Connection connection;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        DBConnectionManager.closeConnection(connection);
    }

    @Test
    void getConnection_ShouldReturnValidConnection() {
        try {
            connection = DBConnectionManager.getConnection();
            assertNotNull(connection);
            assertFalse(connection.isClosed());
        } catch (SQLException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    void closeConnection_ShouldCloseConnection() {
        try {
            connection = DBConnectionManager.getConnection();
            assertFalse(connection.isClosed());

            DBConnectionManager.closeConnection(connection);
            assertTrue(connection.isClosed());
        } catch (SQLException e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}
